package com.pda.assetserver.service;

import com.pda.assetserver.controller.dto.req.MakeConsumptionRequest;
import com.pda.assetserver.controller.resolver.UserRequest;
import com.pda.assetserver.repository.asset.BankAccount;
import com.pda.assetserver.repository.asset.BankAccountRepository;
import com.pda.assetserver.repository.asset.Consumption;
import com.pda.assetserver.repository.asset.ConsumptionRepository;
import com.pda.assetserver.repository.user.User;
import com.pda.assetserver.repository.user.UserRepository;
import com.pda.assetserver.service.dto.req.ConsumptionServiceRequest;
import com.pda.assetserver.service.dto.res.ConsumptionResponse;
import com.pda.assetserver.utils.enums.AccountType;
import com.pda.assetserver.utils.enums.ConsumptionType;
import com.pda.assetserver.utils.exceptions.BadRequestException;
import com.pda.assetserver.utils.exceptions.ForbiddenException;
import com.pda.assetserver.utils.exceptions.InternalServerException;
import com.pda.assetserver.utils.exceptions.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/*
 * 가이드라인
 * 타입: 음식점, 쇼핑, 카, 헤어, 취미, 편의점
 * 배달의민족 10000~32000
 * 메가MGC커피 2000~12000
 * 도죠 9000~12000
 * CU**점 1200~5800
 * 이마트24**점 1200~5800
 * 제주국수 8000~12000
 * 엉터리생고기 8000~14000
 * 금메달유통주 4000~32000
 * 기차순대국 9000~11000
 * 지에스25 1200~5800
 * 주식회사세계 4000~32000
 * 라온석갈비 9000~16000
 * (주)현대백화점 23000~1000000
 * ATOZ피시방 2000~12000
 * 씨제이올리브영 17000~42000
 * 인생네컷: 5000~7000
 * 버거킹: 5200~9600
 * 요거프레소: 4800~15000
 * 홈플러스**점: 18000~54000
 * 탐앤탐스**점: 5000~12000
 * 카카오택시**점 5600~30000
 * 11번가 15000~100000
 * 무신사 15000~400000
 * 쿠팡 15000~100000
 * 블루클럽 17000~56000
 * 애플공식매장 800000~3000000
 * */

@Service
@Transactional
@RequiredArgsConstructor
public class ConsumptionService {
    private final ConsumptionRepository consumptionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final String[] convenienceStore = {
        "CU**점", "이마트24**점", "지에스25",
    };
    private final String[] restaurants = {
        "버거킹","라온석갈비","기차순대국","하이순두부","교동2층집","엉터리생고기", "오레노라멘" ,"구의풍성감자탕", "리춘마라탕", "강동수초밥","(주)쁘띠프랑스", "하미카세"
    };
    private final String[] alcohols = {
        "29포차", "노가리", "인생술집", "금별맥주", "역전할머니맥주**점", "아가네양꼬치",  "무한족발**점","싱싱횟집"
    };
    private final String[] traffics = {
        "카카오택시", "KTX", "고속버스터미널"
    };
    private final String[] delivery = {
        "배달의민족", "쿠팡이츠", "땡겨요"
    };
    private final String[] caffes = {
        "메가MGC커피", "탐앤탐스**점", "요거프레소", "카페테인", "카페텅"
    };
    private final String[] shoppings = {
        "주식회사세계로", "금메달유통주","씨제이올리브영", "홈플러스**점","이마트**점","쿠팡", "11번가", "무신사", "(주)현대백화점", "애플공식매장"
    };
    private final String[] maintains = {
        "블루클럽", "에이바헤어"
    };
    private final String[] hobbies = {
        "ATOZ피시방", "인생네컷", "코인노래방**점"
    };

    public void consumption(final UserRequest userRequest, final MakeConsumptionRequest request) {
        User user = userRepository.findById(userRequest.getSocialIdFront()+"-"+userRequest.getSocialIdBack())
            .orElseThrow(() -> new UnAuthorizedException("고객이 존재하지 않음"));

        if (!user.getContact().equals(userRequest.getContact()))
            throw new ForbiddenException("고객의 전화번호가 옳지 않음");

        BankAccount account = bankAccountRepository.findByAccountNumber(request.getAccountNumber()).orElseThrow(
            () -> new BadRequestException("계좌 존재하지 않음"));

        if (!account.getUser().getId().equals(user.getId())) throw new ForbiddenException("해당 유저의 계좌가 아님");
        if (account.getProduct().getPType().equals("SAVING")) throw new BadRequestException("적금은 사용할 수 없음");
        if (account.getCash() < request.getAmount()) throw new BadRequestException("잔액 부족");


        account.withdraw(request.getAmount());
        bankAccountRepository.save(account);

        consumptionRepository.save(Consumption.builder()
                .dateTime(request.getDateTime())
                .account(account)
                .consumptionType(request.getConsumptionType())
                .amount(request.getAmount())
                .purchasePlace(request.getPurchasePlace())
            .build());
    }

    public List<ConsumptionResponse> getConsumption(final UserRequest userRequest, ConsumptionServiceRequest request) {
        User user = userRepository.findById(userRequest.getSocialIdFront()+"-"+userRequest.getSocialIdBack())
            .orElseThrow(() -> new UnAuthorizedException("고객이 존재하지 않음"));

        if (!user.getContact().equals(userRequest.getContact()))
            throw new ForbiddenException("고객의 전화번호가 옳지 않음");

        List<Consumption> consumptions = consumptionRepository.findByUserIdAndDateTimeBetween(user.getId(),
            LocalDateTime.of(request.getDate(), LocalTime.of(0,0,0)),
            LocalDateTime.of(request.getDate(), LocalTime.of(23,59,59)));

        if (!consumptions.isEmpty())
            return from(consumptions);

        BankAccount account = pickHaveMostCashAccountCanWithdraw(user);
        if (request.getGenerateType().equals("UNDER") && request.getUnderAmount() != null) {
            consumptions = generateUnder(account, request.getUnderAmount(), request.getDate());
            withdrawConsumptions(account, consumptions);
            bankAccountRepository.save(account);
            consumptions = consumptionRepository.saveAll(consumptions);
        } else if (request.getGenerateType().equals("FLEX")) {
            consumptions = generateFlexConsumptions(account, request.getDate());
            withdrawConsumptions(account, consumptions);
            bankAccountRepository.save(account);
            consumptions = consumptionRepository.saveAll(consumptions);
        } else {
            consumptions = generateNormalConsumptions(account, request.getDate());
            withdrawConsumptions(account, consumptions);
            bankAccountRepository.save(account);
            consumptions = consumptionRepository.saveAll(consumptions);
        }

        List<ConsumptionResponse> response = from(consumptions);
        return response.stream()
            .sorted(Comparator.comparing(ConsumptionResponse::getDateTime).reversed())
            .toList();
    }

    private BankAccount pickHaveMostCashAccountCanWithdraw(User user) {
        BankAccount haveMostCashAccount = null;

        for(BankAccount account : bankAccountRepository.findByUser(user)) {
            if (account.getAccountType().equals(AccountType.SAVING))
                continue;

            if (haveMostCashAccount == null)
                haveMostCashAccount = account;

            if (haveMostCashAccount.getCash() < account.getCash())
                haveMostCashAccount = account;
        }

        if (haveMostCashAccount == null)
            throw new InternalServerException("Server Pick Most Cash Account Internal Exception");

        if (haveMostCashAccount.getCash() < 100000)
            throw new BadRequestException("최근 거래내역이 존재하지 않습니다.(돈이 없음)");

        return haveMostCashAccount;
    }

    private List<Consumption> generateFlexConsumptions(BankAccount account, LocalDate date) {
        List<Consumption> consumptions = generateNormalConsumptions(account, date);
        Random random = new Random();
        int randomPick = random.nextInt(6);
        if (randomPick < 2) {
            consumptions.add(Consumption.builder()
                .account(account)
                .purchasePlace(delivery[random.nextInt(delivery.length)])
                .amount(random.nextLong(120, 300)*100)
                .consumptionType(ConsumptionType.DELIVERY)
                .account(account)
                .dateTime(generateLocalDateTime(date))
                .build());
        } else if (randomPick < 4) {
            consumptions.add(Consumption.builder()
                .account(account)
                .purchasePlace("카카오택시")
                .amount(random.nextLong(120, 500)*100)
                .consumptionType(ConsumptionType.TRAFFIC)
                .account(account)
                .dateTime(generateLocalDateTime(date))
                .build());
        } else {
            consumptions.add(Consumption.builder()
                .account(account)
                .purchasePlace(shoppings[random.nextInt(shoppings.length)])
                .amount(random.nextLong(1200, 30000)*100)
                .consumptionType(ConsumptionType.SHOPPING)
                .account(account)
                .dateTime(generateLocalDateTime(date))
                .build());
        }

        return consumptions;
    }

    private List<Consumption> generateNormalConsumptions(BankAccount account, LocalDate date) {
        Random random = new Random();
        List<Consumption> consumptions = new ArrayList<>();
        for(int i=0;i< random.nextInt(1,3);i++) {
            consumptions.add(Consumption.builder()
                    .account(account)
                    .purchasePlace(restaurants[random.nextInt(restaurants.length)])
                    .amount(random.nextLong(8, 18)*1000)
                    .dateTime(generateLocalDateTime(date))
                    .consumptionType(ConsumptionType.RESTAURANT)
                .build());
        }

        if (random.nextBoolean()) {
            consumptions.add(Consumption.builder()
                .account(account)
                .purchasePlace(caffes[random.nextInt(caffes.length)])
                .amount(random.nextLong(20, 80)*100)
                .dateTime(generateLocalDateTime(date))
                .consumptionType(ConsumptionType.CAFFE)
                .build());
        }

        if (random.nextBoolean()) {
            consumptions.add(Consumption.builder()
                .account(account)
                .purchasePlace(hobbies[random.nextInt(hobbies.length)])
                .amount(random.nextLong(20, 50)*100)
                .dateTime(generateLocalDateTime(date))
                .consumptionType(ConsumptionType.HOBBIES)
                .build());
        }

        if (random.nextInt(10) > 8) {
            consumptions.add(Consumption.builder()
                .account(account)
                .purchasePlace(alcohols[random.nextInt(alcohols.length)])
                .amount(random.nextLong(20, 40)*1000)
                .dateTime(generateLocalDateTime(date))
                .consumptionType(ConsumptionType.ALCOHOL)
                .build());
        }

        if (random.nextInt(10) > 8) {
            consumptions.add(Consumption.builder()
                .account(account)
                .purchasePlace(maintains[random.nextInt(maintains.length)])
                .amount(random.nextLong(16, 30)*1000)
                .dateTime(generateLocalDateTime(date))
                .consumptionType(ConsumptionType.MAINTAIN)
                .build());
        }

        return consumptions;
    }

    private List<Consumption> generateUnder(BankAccount account, int amount, LocalDate date) {
        Random random = new Random();
        List<Consumption> consumptions = new ArrayList<>();

        if (amount <= 5000) {
            int avg = amount / 100 / 2;
            for(int i=0;i<2;i++) {
                consumptions.add(Consumption.builder()
                    .consumptionType(ConsumptionType.CONVENIENCE_STORE)
                    .dateTime(generateLocalDateTime(date))
                    .amount((long) random.nextInt(avg-3, avg+2)*100)
                        .account(account)
                    .purchasePlace(convenienceStore[random.nextInt(convenienceStore.length)])
                    .build());
            }

            return consumptions;
        }

        if (amount <= 7000) {
            int avg = amount / 100 / 3;
            for(int i=0;i<3;i++) {
                consumptions.add(Consumption.builder()
                    .consumptionType(ConsumptionType.CONVENIENCE_STORE)
                    .dateTime(generateLocalDateTime(date))
                    .amount((long) random.nextInt(avg-4, avg+2)*100)
                    .account(account)
                    .purchasePlace(convenienceStore[random.nextInt(restaurants.length)])
                    .build());
            }

            return consumptions;
        }

        if (random.nextBoolean())
            return generateUnder(account,random.nextInt(40, 70)*100, date);

        return List.of(Consumption.builder()
            .consumptionType(ConsumptionType.RESTAURANT)
            .dateTime(generateLocalDateTime(date))
            .amount((long) random.nextInt(80, 120)*100)
                .account(account)
            .purchasePlace(restaurants[random.nextInt(restaurants.length-4)])
            .build());
    }

    private void withdrawConsumptions(BankAccount account, List<Consumption> consumptions) {
        try {
            for(Consumption consumption : consumptions)
                account.withdraw(consumption.getAmount());
        } catch (RuntimeException e) {
            throw new BadRequestException("잔액 부족...");
        }
    }

    private LocalDateTime generateLocalDateTime(LocalDate date) {
        Random random = new Random();

        LocalTime time = LocalTime.of(random.nextInt(8, 11),
            random.nextInt(60),
            random.nextInt(60));

        return LocalDateTime.of(date, time);
    }

    private List<ConsumptionResponse> from(List<Consumption> consumptions) {
        return consumptions.stream().map(c -> ConsumptionResponse.builder()
                .consumptionType(c.getConsumptionType())
                .id(c.getId())
                .amount(c.getAmount())
                .dateTime(c.getDateTime())
                .accountNumber(c.getAccount().getAccountNumber())
                .accountName(c.getAccount().getProduct().getName())
                .purchasePlace(c.getPurchasePlace())
                .corpName(c.getAccount().getProduct().getCorporation().getName())
                .corpImage(c.getAccount().getProduct().getCorporation().getProfileImage())
                .build())
            .toList();
    }
}