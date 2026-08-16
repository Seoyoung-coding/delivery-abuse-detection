package com.example.customer.service;
import com.example.customer.domain.Customer;
import com.example.customer.dto.request.LoginRequest;
import com.example.customer.dto.request.SignupRequest;
import com.example.customer.dto.response.AuthResponse;
import com.example.customer.repository.CustomerRepository;
import com.example.global.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public void signup(SignupRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();

        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        String encodedPassword =
                passwordEncoder.encode(password);

        Customer customer = new Customer(
                email,
                encodedPassword
        );

        customerRepository.save(customer);
    }

    public AuthResponse login(LoginRequest request) {

        // 1. 이메일로 회원 찾기
        Customer customer = customerRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("존재하지 않는 이메일입니다.")
                );

        // 2. 입력한 비번과 DB 비밀번호 비교
        boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),
                customer.getPassword()
        );

        // 틀리면 로그인 불가
        if (!passwordMatch) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        // 성공하면 JWT 생성
        String token = jwtTokenProvider.createToken(customer.getEmail());

        // JWT를 AuthResponse에 담아서 controller로 반환
        return new AuthResponse("Login Success", token);
    }

    // 현재 로그인한 사용자 찾기
    public String getMyEmail(String authorizationHeader) {

        // 1. Authorization 헤더가 Bearer로 시작하는지 확인
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new RuntimeException("잘못된 토큰 형식입니다.");
        }

        // 2. "Bearer " 부분을 제거하고 JWT만 꺼내기
        String token = authorizationHeader.substring(7);

        // 3. JWT 안에서 로그인한 사용자의 email 꺼내기
        String email = jwtTokenProvider.getEmailFromToken(token);

        // 4. 해당 email을 가진 회원이 실제 DB에 존재하는지 확인
        Customer customer = customerRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("존재하지 않는 사용자입니다.")
                );

        // 5. 로그인한 사용자의 이메일 반환
        return customer.getEmail();
    }
}