package com.etta.edtech.service;

import com.etta.edtech.config.AuthenticationConfig;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AuthenticationService {

    private final AuthenticationConfig authenticationConfig;

    // aws find user
    // handle error
    // for later autherization
    public User findUser(String accessToken) {

        try {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();

            GetUserResponse response = cognitoClient.getUser(builder -> builder
                    .accessToken(accessToken)
                    .build());

            // Extract specific user attributes
            List<AttributeType> userAttributes = response.userAttributes();
            String sub = getUserAttribute(userAttributes, "sub");
            String preferredUsername = getUserAttribute(userAttributes, "preferred_username");
            String email = getUserAttribute(userAttributes, "email");
            String role = getUserAttribute(userAttributes, "custom:role");


            User userDetails = new User();
            userDetails.setAccessToken(accessToken);
            userDetails.setSub(sub);
            userDetails.setEmail(email);
            userDetails.setUsername(preferredUsername);
            userDetails.setRole(role);


            return userDetails;
        } catch (CognitoIdentityProviderException e) {
            User errorResult = new User();
            errorResult.setError(e.awsErrorDetails().errorMessage());
            return errorResult;
        }
    }

    // Helper method to get a user attribute value by name
    private String getUserAttribute(List<AttributeType> userAttributes, String attributeName) {
        return userAttributes.stream()
                .filter(attribute -> attributeName.equals(attribute.name()))
                .map(AttributeType::value)
                .findFirst()
                .orElse(null);
    }
    public ResponseEntity<String> signUp(String email, String role, String username,
                                         String password) {

        CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        AttributeType attributeTypeEmail = AttributeType.builder()
                .name("email")
                .value(email)
                .build();

        AttributeType attributeTypeUserName = AttributeType.builder()
                .name("preferred_username")
                .value(username)
                .build();

        AttributeType attributeTypeRole = AttributeType.builder()
                .name("custom:role")
                .value(role)
                .build();

        List<AttributeType> attrs = new ArrayList<>();

        attrs.add(attributeTypeEmail);
        attrs.add(attributeTypeUserName);
        attrs.add(attributeTypeRole);

        try {

            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(attrs)
                    .username(email)
                    .clientId(authenticationConfig.getClientId())
                    .password(password)
                    .build();

            identityProviderClient.signUp(signUpRequest);

            return ResponseEntity.ok("User has been signed up");

        }catch (CognitoIdentityProviderException e) {
            return ResponseEntity.badRequest().body(e.awsErrorDetails().errorMessage());
        }

    }

    public ResponseEntity<Object> login(String userName, String password) {
        try {

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
                    .clientId(authenticationConfig.getClientId())
                    .authParameters(authParameters)
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .build();

            AuthenticationResultType authenticationResult = identityProviderClient.initiateAuth(authRequest).authenticationResult();

            User userDetails = findUser(authenticationResult.accessToken());
            //authenticationResult.idToken();

            return ResponseEntity.ok(userDetails);

        } catch(CognitoIdentityProviderException e) {
            String errorMessage = e.awsErrorDetails().errorMessage();
            return ResponseEntity.badRequest().body(errorMessage);
        }

    }

    public ResponseEntity<String> forgotPassword(String email) {
        try {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();


            ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
                    .clientId(authenticationConfig.getClientId())
                    .username(email)
                    .build();

            cognitoClient.forgotPassword(forgotPasswordRequest);



            return ResponseEntity.ok("Password reset code sent successfully");
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.badRequest().body(e.awsErrorDetails().errorMessage());
        }
    }

    public ResponseEntity<String> resetPassword(String email, String newPassword, String resetConfirmationCode) {
        try {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();

            ConfirmForgotPasswordRequest confirmForgotPasswordRequest = ConfirmForgotPasswordRequest.builder()
                    .clientId(authenticationConfig.getClientId())
                    .username(email)
                    .confirmationCode(resetConfirmationCode)
                    .password(newPassword)
                    .build();

            cognitoClient.confirmForgotPassword(confirmForgotPasswordRequest);

            return ResponseEntity.ok("Password reset successfully");
        } catch (CognitoIdentityProviderException e) {
            return ResponseEntity.badRequest().body(e.awsErrorDetails().errorMessage());
        }
    }



}
