package com.etta.edtech.service;

import com.etta.edtech.config.AuthenticationConfig;
import com.etta.edtech.exceptions.InvalidTokenException;
import com.etta.edtech.model.Educator;
import com.etta.edtech.model.User;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class EducatorAuthenticationService {
    private final AuthenticationConfig authenticationConfig;
    public Educator findEducator(String accessToken) {


            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");



            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();

            GetUserResponse response = cognitoClient.getUser(builder -> builder
                    .accessToken(accessToken)
                    .build());

            // Extract specific educator attributes
            List<AttributeType> educatorAttributes = response.userAttributes();
            String sub = getEducatorAttribute(educatorAttributes, "sub");
            String preferredUsername = getEducatorAttribute(educatorAttributes, "preferred_username");
            String email = getEducatorAttribute(educatorAttributes, "email");
            String role = getEducatorAttribute(educatorAttributes, "custom:role");
            String school = getEducatorAttribute(educatorAttributes, "custom:school");

            Educator educatorDetails = new Educator();
            educatorDetails.setAccessToken(accessToken);
            educatorDetails.setSub(sub);
            educatorDetails.setEmail(email);
            educatorDetails.setUsername(preferredUsername);
            educatorDetails.setRole(role);
            educatorDetails.setSchool(school);


            return educatorDetails;


    }

    public boolean verifyUser(String accessToken) {
        AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
        Region awsRegion = Region.of("us-east-1");

        CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                .region(awsRegion)
                .credentialsProvider(() -> awsCredentials)
                .build();

        try {
            cognitoClient.getUser(builder -> builder
                    .accessToken(accessToken)
                    .build());

        } catch (NotAuthorizedException e) {
            return false;
        }
        return true;
    }
    private String getEducatorAttribute(List<AttributeType> educatorAttributes, String attributeName) {
        return educatorAttributes.stream()
                .filter(attribute -> attributeName.equals(attribute.name()))
                .map(AttributeType::value)
                .findFirst()
                .orElse(null);
    }
    public ResponseEntity<String> signUp(Educator educator) {

        CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        AttributeType attributeTypeEmail = AttributeType.builder()
                .name("email")
                .value(educator.getEmail())
                .build();

        AttributeType attributeTypeUserName = AttributeType.builder()
                .name("preferred_username")
                .value(educator.getUsername())
                .build();

        AttributeType attributeTypeRole = AttributeType.builder()
                .name("custom:role")
                .value("EDUCATOR")
                .build();

        AttributeType attributeTypeSchool = AttributeType.builder()
                .name("custom:school")
                .value(educator.getSchool())
                .build();

        AttributeType attributeDateJoined = AttributeType.builder()
                .name("custom:dateJoined")
                .value(String.valueOf(LocalDate.now()))
                .build();

        List<AttributeType> attrs = new ArrayList<>();

        attrs.add(attributeTypeSchool);
        attrs.add(attributeDateJoined);
        attrs.add(attributeTypeEmail);
        attrs.add(attributeTypeUserName);
        attrs.add(attributeTypeRole);



            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(attrs)
                    .username(educator.getEmail())
                    .clientId(authenticationConfig.getEducatorClientId())
                    .password(educator.getPassword())
                    .build();


            identityProviderClient.signUp(signUpRequest);

            return ResponseEntity.ok("Educator has been signed up");



    }

    public ResponseEntity<Object> login(String userName, String password) {


            Map<String,String> authParameters = new HashMap<>();
            authParameters.put("USERNAME", userName);
            authParameters.put("PASSWORD", password);

            AwsCredentialsProvider awsCredentials = StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey())
            );

            CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
                    .region(Region.US_EAST_1)
                    .credentialsProvider(awsCredentials)
                    .build();

            InitiateAuthRequest authRequest = InitiateAuthRequest.builder()
                    .clientId(authenticationConfig.getEducatorClientId())
                    .authParameters(authParameters)
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .build();

            AuthenticationResultType authenticationResult = identityProviderClient.initiateAuth(authRequest).authenticationResult();

            Educator educatorDetails = findEducator(authenticationResult.accessToken());
            //authenticationResult.idToken();

            return ResponseEntity.ok(educatorDetails);



    }

    public ResponseEntity<String> forgotPassword(String email) {

            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();


            ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
                    .clientId(authenticationConfig.getEducatorClientId())
                    .username(email)
                    .build();

            cognitoClient.forgotPassword(forgotPasswordRequest);



            return ResponseEntity.ok("Password reset code sent successfully");

    }

    public ResponseEntity<String> resetPassword(String email, String newPassword, String resetConfirmationCode) {

            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();

            ConfirmForgotPasswordRequest confirmForgotPasswordRequest = ConfirmForgotPasswordRequest.builder()
                    .clientId(authenticationConfig.getEducatorClientId())
                    .username(email)
                    .confirmationCode(resetConfirmationCode)
                    .password(newPassword)
                    .build();

            cognitoClient.confirmForgotPassword(confirmForgotPasswordRequest);

            return ResponseEntity.ok("Password reset successfully");

    }


    public ResponseEntity<Object> updateProfile(String accessToken, Educator educator){

            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();

            UpdateUserAttributesRequest updateEducatorAttributesRequest = UpdateUserAttributesRequest.builder()
                    .accessToken(accessToken)
                    .userAttributes(
                            AttributeType.builder().name("email").value(educator.getEmail()).build(),
                            AttributeType.builder().name("preferred_username").value(educator.getUsername()).build(),
                            AttributeType.builder().name("custom:school").value(educator.getSchool()).build()
                    )
                    .build();

            cognitoClient.updateUserAttributes(updateEducatorAttributesRequest);

            Educator educatorDetails = findEducator(accessToken);

            return ResponseEntity.ok(educatorDetails);

    }


}
