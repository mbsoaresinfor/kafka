package ecommerce.service.order.orchestration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecords;

import ecommerce.service.order.OrchestrationStepStatus;
import ecommerce.service.order.TransactionOrder;
import ecommerce.service.order.orchestration.stepservice.StepEntity;
import ecommerce.service.order.orchestration.stepservice.StepGroup;
import ecommerce.service.order.orchestration.stepservice.StepGroupNames;
import ecommerce.service.order.orchestration.stepservice.StepNames;
import ecommerce.service.order.orchestration.stepservice.StepStatus;

public class OrchestrationNewOrderManager {

	//private final TransactionOrder transactionOrder;
	private final Map<String,OrchestrationNewOrderEntity> mapOrchestrationNewOrder = new HashMap<>();
	private final Map<StepGroupNames, StepGroup> mapGroupStage = new HashMap<StepGroupNames, StepGroup>();
	private final Map<StepGroupNames, OrchestrationStepStatus> orchStepStatusOfGroupStageService = new HashMap<>();

	public OrchestrationNewOrderManager() {		
		this.initGroupStage();
	}

	public void startNewOrchestration(TransactionOrder transactionOrder) {
		
	}
	public void updateOrchestration(String idTransactionOrder, StepGroupNames groupStageName,
			StepNames stageName, StepStatus stageStatus) {

		for (var id : listIdsOrchestrationNewOrder) {
			OrchestrationNewOrderEntity orchestrationStep = mapOrchestrationNewOrder.get(record.value().id);
			if (orchestrationStep != null) {
				StepGroup groupStage = orchestrationStep.getGroupStage(groupStageName);
				StepStatus stageStatusForUpdate = groupStage.getStages(stageName).get();
				stageStatusForUpdate = stageStatus;
			}
		}
	}

	private void initGroupStage() {
		StepGroup groupStageFinancial = new StepGroup(StepGroupNames.FINANCIAL);
		groupStageFinancial
				.addStages(new StepEntity(StepNames.DETECTOR_FRAUDE_SERVICE, StepStatus.UNPROCESSED));
		groupStageFinancial
				.addStages(new StepEntity(StepNames.FINANCIAL_SERVICE, StepStatus.UNPROCESSED));
		mapGroupStage.put(StepGroupNames.FINANCIAL, groupStageFinancial);
		orchStepStatusOfGroupStageService.put(StepGroupNames.FINANCIAL, OrchestrationStepStatus.UNPROCESSED);
	}

	public StepGroup getGroupStage(StepGroupNames nameGroup) {
		return mapGroupStage.get(nameGroup);
	}

}
