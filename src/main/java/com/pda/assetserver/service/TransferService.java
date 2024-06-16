package com.pda.assetserver.service;

import com.pda.assetserver.repository.asset.BankAccount;
import com.pda.assetserver.repository.asset.BankAccountRepository;
import com.pda.assetserver.repository.user.User;
import com.pda.assetserver.repository.user.UserRepository;
import com.pda.assetserver.service.dto.req.TransferServiceRequest;
import com.pda.assetserver.utils.enums.AccountType;
import com.pda.assetserver.utils.exceptions.BadRequestException;
import com.pda.assetserver.utils.exceptions.ForbiddenException;
import com.pda.assetserver.utils.exceptions.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public void transfer(final TransferServiceRequest request) {
        User user = userRepository.findById(request.getFrontId()+"-"+request.getBackId())
            .orElseThrow(() -> new UnAuthorizedException("해당 고객은 존재하지 않습니다."));

        if (!user.getContact().equals(request.getContact()))
            throw new ForbiddenException("고객 권한 없음");

        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(request.getFromAccountNumber())
            .orElseThrow(() -> new BadRequestException("계좌가 존재하지 않음"));
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(request.getToAccountNumber())
            .orElseThrow(() -> new BadRequestException("계좌가 존재하지 않음"));


        if (!isOwner(fromAccount, user))
            throw new ForbiddenException("출금 계좌 주인 아님");

        if (fromAccount.equals(toAccount))
            throw new BadRequestException("동일 계좌");

        if (fromAccount.getAccountType().equals(AccountType.SAVING))
            throw new BadRequestException("적금에서는 이체가 불가능");

        if (fromAccount.getCash() < request.getAmount())
            throw new BadRequestException("잔액이 부족합니다");

        fromAccount.withdraw(request.getAmount());
        toAccount.deposit(request.getAmount());

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
    }

    private boolean isOwner(BankAccount account,User user) {
       return account.getUser().getContact().equals(user.getContact())
           && account.getUser().getId().equals(user.getId());
    }
}
