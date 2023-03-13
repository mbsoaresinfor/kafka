package ecommerce.service.order.orchestration;

import ecommerce.service.order.OrchestrationStepStatus;
import ecommerce.service.order.orchestration.stepservice.StepGroup;

public record OrchestrationNewOrderEntity(String id,StepGroup groupStageService, OrchestrationStepStatus orchestrationStepStatus) {

}
