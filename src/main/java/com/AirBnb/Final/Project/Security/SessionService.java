package com.AirBnb.Final.Project.Security;


import com.AirBnb.Final.Project.Entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessionService {

    private final SessionRepository sessionRepository;

    public void generateSession(User user,String refreshToken){

        log.info("session generation start here");

        List<Session>sessions=sessionRepository.findByUser(user);

        if (sessions.size()==1){

            sessions.sort(Comparator.comparing(Session::getLastUsedAt));

            Session leastUsedSession=sessions.get(0);

            sessionRepository.delete(leastUsedSession);
        }

        Session newSession=Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        sessionRepository.save(newSession);

        log.info("session generation done successfully");
    }

    @Transactional
    public void verifySession(String refreshToken){

        log.info("verify session start here");

        Session session=sessionRepository.findByRefreshToken(refreshToken)
                .orElseThrow(()->new SessionAuthenticationException("session is expired"));

        session.setLastUsedAt(LocalDateTime.now());

        log.info("verify session done successfully");
    }

}
