package com.pm.billingservice.grpc;

import billing.BillingRequest;
import billing.BillingResponse;
import billing.BillingServiceGrpc;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class BillingGrpcService extends BillingServiceGrpc.BillingServiceImplBase {

    @Override
    public void createBillingAccount(
            BillingRequest request,
            StreamObserver<BillingResponse> responseObserver
    ) {

//        GUARD CLAUSE

        if (request.getId().isBlank()
                || request.getName().isBlank()
                || request.getEmail().isBlank()) {

            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("id, name, and email are required")
                            .asRuntimeException()
            );
            return;
        }

//        BUSINESS LOGIC
        BillingResponse response = BillingResponse.newBuilder()
                .setAccountId("123")
                .setStatus("ACTIVE")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
