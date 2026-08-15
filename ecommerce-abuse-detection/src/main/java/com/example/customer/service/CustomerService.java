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
        Customer customer = customerRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("존재하지 않는 이메일입니다.")
                );

        boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),
                customer.getPassword()
        );

        if (!passwordMatch) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }

        String token = jwtTokenProvider.createToken(customer.getEmail());

        return new AuthResponse("Login Success", token);
    }
}