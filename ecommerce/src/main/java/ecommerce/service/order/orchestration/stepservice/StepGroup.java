package ecommerce.service.order.orchestration.stepservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.common.message.ApiMessageType.ListenerType;

record StepGroup(StepGroupNames name,List<StepEntity> listStages) {

	
	public StepGroup(String name) {
		this(null,new ArrayList<>());
	}
	
	public StepGroup(StepGroupNames financial) {
		this(null,new ArrayList<>());
	}
	
	public void addStages(StepEntity stage) {
		this.listStages.add(stage);
	}

	public Optional<StepStatus> getStages(StepNames stageName) {
		return listStages
				.stream()
				.filter((s)-> s.stageName().equals(stageName))
				.map((s)-> s.stageStatus())
				.findFirst();
	}
	
//	public StageStatus getStatus() {
//		Optional<Integer> ret = listStage
//		.stream()
//		.map((p)-> p.stageStatus().ordinal())
//		.max(Comparable::compareTo);
//		return null; // TODO implementar.
//	}
}
