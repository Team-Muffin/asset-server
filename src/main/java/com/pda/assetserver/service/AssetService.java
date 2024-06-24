package com.pda.assetserver.service;

import com.pda.assetserver.controller.resolver.UserRequest;
import com.pda.assetserver.repository.asset.Asset;
import com.pda.assetserver.repository.asset.AssetRepository;
import com.pda.assetserver.repository.asset.BankAccount;
import com.pda.assetserver.repository.asset.BankAccountRepository;
import com.pda.assetserver.repository.asset.CardAsset;
import com.pda.assetserver.repository.asset.CardAssetRepository;
import com.pda.assetserver.repository.asset.StockAsset;
import com.pda.assetserver.repository.asset.StockAssetRepository;
import com.pda.assetserver.repository.product.Product;
import com.pda.assetserver.repository.product.ProductRepository;
import com.pda.assetserver.repository.product.banking.DepositProduct;
import com.pda.assetserver.repository.product.banking.DepositProductRepository;
import com.pda.assetserver.repository.product.banking.SavingProduct;
import com.pda.assetserver.repository.product.banking.SavingProductRepository;
import com.pda.assetserver.repository.product.card.CardProduct;
import com.pda.assetserver.repository.product.card.CardRepository;
import com.pda.assetserver.repository.product.stock.Stock;
import com.pda.assetserver.repository.product.stock.StockRepository;
import com.pda.assetserver.repository.user.User;
import com.pda.assetserver.repository.user.UserRepository;
import com.pda.assetserver.service.dto.req.GetAssetInfoServiceRequest;
import com.pda.assetserver.service.dto.res.AccountResponse;
import com.pda.assetserver.service.dto.res.AssetInfoResponse;
import com.pda.assetserver.service.dto.res.AssetSummaryResponse;
import com.pda.assetserver.service.dto.res.CardResponse;
import com.pda.assetserver.service.dto.res.PortfolioResponse;
import com.pda.assetserver.utils.cash.CashUtils;
import com.pda.assetserver.utils.enums.AccountType;
import com.pda.assetserver.utils.exceptions.ForbiddenException;
import com.pda.assetserver.utils.exceptions.InternalServerException;
import com.pda.assetserver.utils.exceptions.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AssetService {
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SavingProductRepository savingProductRepository;
    private final DepositProductRepository depositProductRepository;
    private final BankAccountRepository bankAccountRepository;
    private final StockAssetRepository stockAssetRepository;
    private final StockRepository stockRepository;
    private final AssetRepository assetRepository;
    private final CardAssetRepository cardAssetRepository;
    private final CardRepository cardRepository;

    @Transactional
    public AssetInfoResponse getAssetInfo(final GetAssetInfoServiceRequest request) {
        AssetInfoResponse result = getUserInfoWithAll(request);
        if (request.getTargets().contains("ALL"))
            return result;

        AssetInfoResponse.AssetInfoResponseBuilder builder = AssetInfoResponse.builder();
        if (request.getTargets().contains("ACCOUNT"))
            builder.accounts(result.getAccounts());

        if (request.getTargets().contains("CARD"))
            builder.cards(result.getCards());

        if (request.getTargets().contains("PORTFOLIO")) {
            builder.portfolio(result.getPortfolio());
            builder.portfolioReturnRate(result.getPortfolioReturnRate());
        }

        if (request.getTargets().contains("LOAN"))
            builder.loans(result.getLoans());

        if (request.getTargets().contains("FUND"))
            builder.funds(result.getFunds());

        return builder.build();
    }

    public List<AssetSummaryResponse> getAssetSummary(final UserRequest userRequest) {
        String socialId = userRequest.getSocialIdFront()+"-"+userRequest.getSocialIdBack();
        User user = userRepository.findById(socialId)
            .orElseThrow(() -> new UnAuthorizedException("고객이 존재하지 않음"));

        if (!user.getContact().equals(userRequest.getContact()))
            throw new ForbiddenException("고객 권한 없음");

        List<AssetSummaryResponse> response = new ArrayList<>();
        for(Asset asset:  assetRepository.findByUser(user)) {
            Product product = asset.getProduct();

            if (product.getPType().equals("CARD")) {
                response.add(AssetSummaryResponse.builder()
                        .name(product.getName())
                        .productType(product.getPType())
                        .thumbnail(((CardProduct) product).getCardImage())
                    .build());
            } else {
                response.add(AssetSummaryResponse.builder()
                        .name(product.getName())
                        .productType(product.getPType())
                        .thumbnail(product.getCorporation().getProfileImage())
                        .corpName(product.getCorporation().getName())
                    .build());
            }
        }

        return response;
    }

    private AssetInfoResponse getUserInfoWithAll(final GetAssetInfoServiceRequest request) {
        String socialId = request.getFrontId()+"-"+request.getBackId();
        Optional<User> optionalUser = userRepository.findById(socialId);

        if (optionalUser.isEmpty())
            return generateAllAsset(socialId, request);


        if (!optionalUser.get().getContact().equals(request.getContact()))
            throw new ForbiddenException("고객 권한 없음");

        List<? extends Asset> assets = assetRepository.findByUser(optionalUser.get());
        List<BankAccount> accounts = new ArrayList<>();
        List<StockAsset> stocks = new ArrayList<>();
        List<CardAsset> cards = new ArrayList<>();
        double portfolioReturnRate = 0.0;

        for(Asset asset : assets) {
            if (asset instanceof BankAccount) {
                BankAccount account = (BankAccount) asset;
                accounts.add(account);

                if (account.getAccountType() == AccountType.CMA) {
                    stocks.addAll(account.getStockAssets());
                    portfolioReturnRate = account.getReturnRate();
                }
            } else if (asset instanceof CardAsset) {
                CardAsset card = (CardAsset) asset;
                cards.add(card);
            }
        }

        return AssetInfoResponse.builder()
            .accounts(toAccountResponseListFrom(accounts))
            .portfolio(toPortfolioResponseListFrom(stocks))
            .cards(toCardResponseListFrom(cards))
            .portfolioReturnRate(portfolioReturnRate)
            .build();
    }

    private AssetInfoResponse generateAllAsset(String socialId, GetAssetInfoServiceRequest request) {
        User user = generateUser(socialId, request.getContact());
        List<BankAccount> accounts = generateBankAsset(user);

        BankAccount cmaAccount = null;
        for (BankAccount account : accounts) {
            if (account.getAccountType() == AccountType.CMA) {
                cmaAccount = account;
            }
        }

        List<StockAsset> stocksAssets = generateStock(cmaAccount);
        List<CardAsset> cardAssets = generateCardAsset(user);

        return AssetInfoResponse.builder()
            .accounts(toAccountResponseListFrom(accounts))
            .portfolio(toPortfolioResponseListFrom(stocksAssets))
            .cards(toCardResponseListFrom(cardAssets))
            .portfolioReturnRate(cmaAccount != null? cmaAccount.getReturnRate():null)
            .build();
    }

    private User generateUser(String socialId, String contact) {
        return userRepository.save(User.builder()
                .id(socialId)
                .contact(contact)
            .build());
    }

    private List<BankAccount> generateBankAsset(User user) {
        List<Product> products = pickRandomBankAccounts();
        Random random = new Random();

        List<BankAccount> bankAccounts = products.stream()
            .<BankAccount>map(product -> BankAccount.builder()
                .user(user)
                .product(product)
                .accountNumber(generateAccountNumber())
                .cash((long) CashUtils.generateCash(random.nextInt(3)+5))
                .accountType(AccountType.valueOf(product.getPType()))
                .returnRate(product.getPType().equals("CMA")?generateReturnRate():null)
                .build())
            .toList();

        return bankAccountRepository.saveAll(bankAccounts);
    }

    private Double generateReturnRate() {
        Random random = new Random();
        int bound = random.nextInt(10);

        if (bound < 2)
            return random.nextDouble(-80, 10);

        if (bound < 7)
            return random.nextDouble(0, 20);

        if (bound < 9)
            return random.nextDouble(10, 100);

        return random.nextDouble(80, 400);
    }

    // get random deposit / saving product and 1 cma product
    private List<Product> pickRandomBankAccounts() {
        List<DepositProduct> deposits = depositProductRepository.findAll();
        Collections.shuffle(deposits);

        List<SavingProduct> savings = savingProductRepository.findAll();
        Collections.shuffle(savings);

        Random random = new Random();
        int depositPick = random.nextInt(3)+2;
        int savingsPick = random.nextInt(3)+2;

        List<Product> products = new ArrayList<>();
        for(int i=0; i<depositPick; i++)
            products.add(deposits.get(i));
        for(int i=0; i<savingsPick; i++)
            products.add(savings.get(i));

        products.add(productRepository.findById(144L).orElseThrow(
            () -> new InternalServerException("144에 해당하는 CMA 계좌를 찾지 못했습니다.")));

        return products;
    }

    private String generateAccountNumber() {
        String accountNumber = randomGenerateAccountNumber();

        while (bankAccountRepository.existsByAccountNumber(accountNumber))
            accountNumber = randomGenerateAccountNumber();

        return accountNumber;
    }

    // 실제 계좌 번호가 아닙니다
    private String randomGenerateAccountNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i=0;i<14;i++) {
            if (i == 0) {
                sb.append(random.nextInt(9)+1);
                continue;
            }
            sb.append(random.nextInt(10));
        }

        sb.insert(3, '-');
        sb.insert(8,'-');

        return sb.toString();
    }

    private String generateCardNumber() {
        String cardNumber= randomGenerateCardNumber();

        while (cardAssetRepository.existsByCardNumber(cardNumber))
            cardNumber = randomGenerateCardNumber();

        return cardNumber;
    }

    private String randomGenerateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for (int i=0;i<16;i++) {
            if (i == 0) {
                sb.append(random.nextInt(9)+1);
                continue;
            }
            sb.append(random.nextInt(10));
        }

        sb.insert(4, '-');
        sb.insert(9,'-');
        sb.insert(14,'-');

        return sb.toString();
    }

    private List<StockAsset> generateStock(BankAccount bankAccount) {
        Random random = new Random();
        List<StockAsset> stockAssets = new ArrayList<>();

        // 100개 이상의 주식이 있으므로 괜찮다
        int numOfStocks = random.nextInt(5)+4;
        List<Stock> domesticStocks = stockRepository.findAllByStockType("D");

        Collections.shuffle(domesticStocks);
        for (int i=0; i<numOfStocks; i++) {
            stockAssets.add(StockAsset.builder()
                    .account(bankAccount)
                    .stock(domesticStocks.get(i))
                    .quantity(random.nextInt(100)+1)
                .build());
        }

        numOfStocks = random.nextInt(7)+4;
        List<Stock> foreignStocks = stockRepository.findAllByStockType("F");
        Collections.shuffle(foreignStocks);
        for (int i=0; i<numOfStocks; i++) {
            stockAssets.add(StockAsset.builder()
                .account(bankAccount)
                .stock(foreignStocks.get(i))
                .quantity(random.nextInt(100)+1)
                .build());
        }

        return stockAssetRepository.saveAll(stockAssets);
    }

    // 3개 만들기
    private List<CardAsset> generateCardAsset(User user) {
        List<CardProduct> cardProducts = cardRepository.findAll();
        Collections.shuffle(cardProducts);

        List<CardAsset> cardAssets = new ArrayList<>();
        for (int i=0; i<3;i++) {
            cardAssets.add(CardAsset.builder()
                    .user(user)
                    .product(cardProducts.get(i))
                    .cardNumber(generateCardNumber())
                .build());
        }

        return cardAssetRepository.saveAll(cardAssets);
    }

    private List<AccountResponse> toAccountResponseListFrom(List<BankAccount> accounts) {
        return accounts.stream()
            .map((account) -> AccountResponse.builder()
                .name(account.getProduct().getName())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType().name())
                .cash(account.getCash())
                .id(account.getId())
                .corpName(account.getProduct().getCorporation().getName())
                .logo(account.getProduct().getCorporation().getProfileImage())
                .returnRate(account.getReturnRate())
                .build())
            .toList();
    }

    private List<PortfolioResponse> toPortfolioResponseListFrom(List<StockAsset> stockAssets) {
        return stockAssets.stream()
            .map((stockAsset) -> {
                Stock stock = stockAsset.getStock();
                if (stock.getStockType().equals("F")) {
                    return PortfolioResponse.builder()
                        .price(CashUtils.dollarToWon(stock.getPrice()))
                        .code(stock.getCode())
                        .name(stock.getName())
                        .engName(stock.getEnglishName())
                        .quantity(stockAsset.getQuantity())
                        .stockType("F")
                        .dollar(stock.getPrice())
                        .build();
                }

                return PortfolioResponse.builder()
                    .price((int) stock.getPrice())
                    .code(stock.getCode())
                    .name(stock.getName())
                    .stockType("D")
                    .quantity(stockAsset.getQuantity())
                    .build();
            })
            .toList();
    }

    private List<CardResponse> toCardResponseListFrom(List<CardAsset> cardAssets) {
        return cardAssets.stream()
            .map(cardAsset -> {
                CardProduct card = (CardProduct) cardAsset.getProduct();
                return CardResponse.builder()
                .id(cardAsset.getId())
                .corpName(cardAsset.getProduct().getCorporation().getName())
                .cardNumber(cardAsset.getCardNumber())
                .image(card.getCardImage())
                .name(card.getName())
                .build();
            })
            .toList();
    }
}
