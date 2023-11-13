package project.como.domain.mail.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {
    // 1. UUID 생성
    // 2. Redis에 Email, UUID로 저장
    // 3. 메일을 보냄 url은 http://localhost:3000/인증 ~~
    // 4. 클릭을 하면 클라를 통해 백으로 요청을 uuid가 오게 됨
    // 5. 요청이 온 token 값과 redis의 값을 비교함
    // 6. 비교해서 같으면 클라이언트에 성공을 보내줌
}
