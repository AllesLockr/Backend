package eu.luftiger.cae.application.task.gateway

import eu.luftiger.cae.application.common.gateway.ReadWriteGateway
import eu.luftiger.cae.domain.task.Task
import eu.luftiger.cae.domain.task.TaskId

interface TaskGateway : ReadWriteGateway<Task, TaskId>