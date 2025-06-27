package com.juul.kable.logs

import com.juul.kable.logs.Logging.DataProcessor.Operation
import com.juul.kable.toByteArray
import com.juul.kable.toUuid
import platform.CoreBluetooth.CBCharacteristic
import platform.CoreBluetooth.CBDescriptor
import platform.CoreBluetooth.CBService
import platform.Foundation.NSData
import platform.Foundation.NSError

internal actual val LOG_INDENT: String? = "  "

internal fun LogMessage.detail(data: NSData?, operation: Operation) {
    detail(data?.toByteArray(), operation)
}

internal fun LogMessage.detail(error: NSError?) {
    if (error != null) detail("error", error.toString())
}

internal fun LogMessage.detail(service: CBService) {
    detail("service", service.UUID.UUIDString)
}

internal fun LogMessage.detail(characteristic: CBCharacteristic) {
    detail(
        characteristic.service?.UUID?.toUuid().toString(),
        characteristic.UUID.toUuid(),
    )
}

internal fun LogMessage.detail(descriptor: CBDescriptor) {
    descriptor.characteristic?.let { characteristic ->
        characteristic.service?.let { service ->
            detail(
                service.UUID.toUuid(),
                characteristic.UUID.toUuid(),
                descriptor.UUID.toUuid(),
            )
        } ?: run {
            detail("error", "Service is null for descriptor ${descriptor.UUID.UUIDString}")
        }
    } ?: run {
        detail("error", "Characteristic is null for descriptor ${descriptor.UUID.UUIDString}")
    }
}
