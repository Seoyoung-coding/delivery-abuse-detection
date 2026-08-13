package com.example.user.service;
import com.example.user.domain.Customer;
import com.example.user.dto.CustomerDto;
import com.example.user.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public void signup(CustomerDto.SignupRequest request) {

        // 1. 프론트에서 받은 email 꺼내기
        String email = request.getEmail();
        String password = request.getPassword();


        // 2. 이미 가입된 이메일인지 검사
        if (customerRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }


        // 3. 비밀번호 암호화
        String encodedPassword =
                passwordEncoder.encode(password);


        // 4. Customer 객체 만들기
        Customer customer = new Customer(
                email,
                encodedPassword
        );


        // 5. DB에 저장
        customerRepository.save(customer);
    }
}