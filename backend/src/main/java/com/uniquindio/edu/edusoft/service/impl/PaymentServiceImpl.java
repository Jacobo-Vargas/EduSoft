package com.uniquindio.edu.edusoft.service.impl;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.resources.preference.Preference;
import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.service.CourseService;
import com.uniquindio.edu.edusoft.service.EnrollmentService;
import com.uniquindio.edu.edusoft.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${app.url.base}")
    private String appUrlBase;

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    @Override
    public ResponseEntity<?> getInitPoint(Long courseId, Authentication authentication) throws Exception {

        Course course = courseService.getCourseById(courseId);

        validateEnrollment(courseId, authentication);
        String url = course.getCoverUrl();
        BigDecimal amount = course.getPrice();

        MercadoPagoConfig.setAccessToken(accessToken);
        PreferenceItemRequest itemRequest =
                PreferenceItemRequest.builder()
                        .id(String.valueOf(courseId))
                        .title("Courses")
                        .description("PS5")
                        .pictureUrl(url)
                        .categoryId(course.getDescription())
                        .quantity(1)
                        .currencyId("COP")
                        .unitPrice(amount)
                        .build();
        List<PreferenceItemRequest> items = new ArrayList<>();
        items.add(itemRequest);
        PreferenceRequest preferenceRequest = PreferenceRequest
                .builder()
                .items(items)
                .externalReference(courseId.toString())
                .backUrls(getBackUrls())
                .build();
        PreferenceClient client = new PreferenceClient();
        Preference preference = client.create(preferenceRequest);

        Map<String, String> response = new HashMap<>();
        response.put("init_point", preference.getInitPoint());
        response.put("id", preference.getId());

        log.info("Pago: {}", preference.getId());
        return ResponseEntity.ok(response);
    }

    private PreferenceBackUrlsRequest getBackUrls() {
        log.info("getBackUrls {}", appUrlBase);
        return PreferenceBackUrlsRequest.builder()
                .success(appUrlBase + "/successPayment")
                .pending(appUrlBase + "/pendingPayment")
                .failure(appUrlBase + "/failurePayment")
                .build();
    }

    private void validateEnrollment(Long courseId, Authentication authentication) throws Exception {
        boolean flag = enrollmentService.alreadyEnrolled(courseId, authentication);

        if (flag) {
            throw new Exception("Ya te encuentras registrado en este curso");
        }
    }
}
