package com.etta.edtech.service;

import com.etta.edtech.config.AuthenticationConfig;
import com.etta.edtech.exceptions.InvalidTokenException;
import com.etta.edtech.model.ErrorMessage;
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
import java.util.*;

@Service
@AllArgsConstructor
public class UserAuthenticationService {

    private final AuthenticationConfig authenticationConfig;

    public User findUser(String accessToken) throws NotAuthorizedException {

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

            String studentFirstname = getUserAttribute(userAttributes, "custom:studentFirstname");
            String studentLastname = getUserAttribute(userAttributes, "custom:studentLastname");
            String parentFirstname = getUserAttribute(userAttributes, "custom:parentFirstname");
            String parentLastname = getUserAttribute(userAttributes, "custom:parentLastname");
            String parentPhoneNumber = getUserAttribute(userAttributes, "custom:parentPhoneNumber");
            String parentEmailAddress = getUserAttribute(userAttributes, "custom:parentEmailAddress");
            String school = getUserAttribute(userAttributes, "custom:school");
            String dateJoined = getUserAttribute(userAttributes, "custom:dateJoined");
            String grade = getUserAttribute(userAttributes, "custom:grade");
            String age = getUserAttribute(userAttributes, "custom:age");


            User userDetails = new User();
            userDetails.setAccessToken(accessToken);
            userDetails.setSub(sub);
            userDetails.setEmail(email);
            userDetails.setUsername(preferredUsername);
            userDetails.setRole(role);
            userDetails.setStudentFirstname(studentFirstname);
            userDetails.setStudentLastname(studentLastname);
            userDetails.setParentFirstname(parentFirstname);
            userDetails.setParentLastname(parentLastname);
            userDetails.setParentPhoneNumber(parentPhoneNumber);
            userDetails.setParentEmailAddress(parentEmailAddress);
            userDetails.setSchool(school);
            userDetails.setDateJoined(dateJoined);
            userDetails.setGrade(grade);
            userDetails.setAge(age);


            return userDetails;


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


    private String getUserAttribute(List<AttributeType> userAttributes, String attributeName) {
        return userAttributes.stream()
                .filter(attribute -> attributeName.equals(attribute.name()))
                .map(AttributeType::value)
                .findFirst()
                .orElse(null);
    }
    public ResponseEntity<String> signUp(User user) {

        CognitoIdentityProviderClient identityProviderClient = CognitoIdentityProviderClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(ProfileCredentialsProvider.create())
                .build();

        AttributeType attributeTypeEmail = AttributeType.builder()
                .name("email")
                .value(user.getEmail())
                .build();

        AttributeType attributeTypeUserName = AttributeType.builder()
                .name("preferred_username")
                .value(user.getUsername())
                .build();

        AttributeType attributeTypeRole = AttributeType.builder()
                .name("custom:role")
                .value("USER")
                .build();

        AttributeType attributeTypeStudentFirstname = AttributeType.builder()
                .name("custom:studentFirstname")
                .value(user.getStudentFirstname())
                .build();

        AttributeType attributeTypeStudentLastname = AttributeType.builder()
                .name("custom:studentLastname")
                .value(user.getStudentLastname())
                .build();

        AttributeType attributeTypeParentFirstname = AttributeType.builder()
                .name("custom:parentFirstname")
                .value(user.getParentFirstname())
                .build();

        AttributeType attributeTypeParentLastname = AttributeType.builder()
                .name("custom:parentLastname")
                .value(user.getParentLastname())
                .build();

        AttributeType attributeTypeParentPhoneNumber = AttributeType.builder()
                .name("custom:parentPhoneNumber")
                .value(user.getParentPhoneNumber())
                .build();

        AttributeType attributeTypeParentEmailAddress = AttributeType.builder()
                .name("custom:parentEmailAddress")
                .value(user.getParentEmailAddress())
                .build();

        AttributeType attributeTypeSchool = AttributeType.builder()
                .name("custom:school")
                .value(user.getSchool())
                .build();

        AttributeType attributeTypeGrade = AttributeType.builder()
                .name("custom:grade")
                .value(user.getGrade())
                .build();

        AttributeType attributeTypeAge = AttributeType.builder()
                .name("custom:age")
                .value(String.valueOf(user.getAge()))
                .build();

        AttributeType attributeDateJoined = AttributeType.builder()
                .name("custom:dateJoined")
                .value(String.valueOf(LocalDate.now()))
                .build();


        List<AttributeType> attrs = new ArrayList<>();

        attrs.add(attributeTypeEmail);
        attrs.add(attributeTypeUserName);
        attrs.add(attributeTypeRole);
        attrs.add(attributeTypeStudentFirstname);
        attrs.add(attributeTypeStudentLastname);
        attrs.add(attributeTypeParentFirstname);
        attrs.add(attributeTypeParentLastname);
        attrs.add(attributeTypeParentPhoneNumber);
        attrs.add(attributeTypeParentEmailAddress);
        attrs.add(attributeTypeSchool);
        attrs.add(attributeTypeGrade);
        attrs.add(attributeTypeAge);
        attrs.add(attributeDateJoined);

            SignUpRequest signUpRequest = SignUpRequest.builder()
                    .userAttributes(attrs)
                    .username(user.getEmail())
                    .clientId(authenticationConfig.getUserClientId())
                    .password(user.getPassword())
                    .build();

            identityProviderClient.signUp(signUpRequest);

            return ResponseEntity.ok("User has been signed up");



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
                    .clientId(authenticationConfig.getUserClientId())
                    .authParameters(authParameters)
                    .authFlow(AuthFlowType.USER_PASSWORD_AUTH)
                    .build();

            AuthenticationResultType authenticationResult = identityProviderClient.initiateAuth(authRequest).authenticationResult();

            User userDetails = findUser(authenticationResult.accessToken());


            return ResponseEntity.ok(userDetails);


    }

    public ResponseEntity<String> forgotPassword(String email) {

            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();


            ForgotPasswordRequest forgotPasswordRequest = ForgotPasswordRequest.builder()
                    .clientId(authenticationConfig.getUserClientId())
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
                    .clientId(authenticationConfig.getUserClientId())
                    .username(email)
                    .confirmationCode(resetConfirmationCode)
                    .password(newPassword)
                    .build();

            cognitoClient.confirmForgotPassword(confirmForgotPasswordRequest);

            return ResponseEntity.ok("Password reset successfully");

    }


    public ResponseEntity<Object> updateProfile(String accessToken, User user) {
            AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(authenticationConfig.getAccessKey(), authenticationConfig.getSecretKey());
            Region awsRegion = Region.of("us-east-1");

            CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder()
                    .region(awsRegion)
                    .credentialsProvider(() -> awsCredentials)
                    .build();

            UpdateUserAttributesRequest updateUserAttributesRequest = UpdateUserAttributesRequest.builder()
                    .accessToken(accessToken)
                    .userAttributes(
                            AttributeType.builder().name("email").value(user.getEmail()).build(),
                            AttributeType.builder().name("preferred_username").value(user.getUsername()).build(),
                            AttributeType.builder().name("custom:studentFirstname").value(user.getStudentFirstname()).build(),
                            AttributeType.builder().name("custom:studentLastname").value(user.getStudentLastname()).build(),
                            AttributeType.builder().name("custom:parentFirstname").value(user.getParentFirstname()).build(),
                            AttributeType.builder().name("custom:parentLastname").value(user.getParentLastname()).build(),
                            AttributeType.builder().name("custom:parentPhoneNumber").value(user.getParentPhoneNumber()).build(),
                            AttributeType.builder().name("custom:parentEmailAddress").value(user.getParentEmailAddress()).build(),
                            AttributeType.builder().name("custom:school").value(user.getSchool()).build(),
                            AttributeType.builder().name("custom:grade").value(user.getGrade()).build(),
                            AttributeType.builder().name("custom:age").value(String.valueOf(user.getAge())).build()


                    )
                    .build();

            cognitoClient.updateUserAttributes(updateUserAttributesRequest);

            User userDetails = findUser(accessToken);

            return ResponseEntity.ok(userDetails);

    }


}
