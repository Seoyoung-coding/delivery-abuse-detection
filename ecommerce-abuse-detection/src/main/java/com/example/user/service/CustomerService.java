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

    public void login(CustomerDto.LoginRequest request) {
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
    }

}