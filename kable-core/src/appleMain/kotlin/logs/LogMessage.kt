package com.juul.kable.logs

import com.juul.kable.logs.Logging.DataProcessor.Operation
import com.juul.kable.toByteArray
import com.juul.kable.toUuid
import platform.CoreBluetooth.CBAttribute
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

private fun LogMessage.detailService(service: CBService? = null) {
    detail("service", service?.UUID?.UUIDString ?: "Unknown UUID")
}

private fun LogMessage.detailCharacteristic(characteristic: CBCharacteristic) {
    val serviceUuid = characteristic.service
        ?.UUID
        ?.toUuid()
    if (serviceUuid == null) {
        detail("service", "Unknown (null value)")
        return
    }

    detail(serviceUuid, characteristic.UUID.toUuid())
}

private fun LogMessage.detailDescriptor(descriptor: CBDescriptor) {
    val characteristic = descriptor.characteristic
    if (characteristic == null) {
        detail("characteristic", "Unknown (null value)")
        return
    }

    val serviceUuid = characteristic.service
        ?.UUID
        ?.toUuid()

    if (serviceUuid == null) {
        detail("service", "Unknown (null value)")
        return
    }

    detail(
        serviceUuid,
        characteristic.UUID.toUuid(),
        descriptor.UUID.toUuid(),
    )
}

internal fun LogMessage.detail(attribute: CBAttribute){
    when(attribute) {
        is CBService -> detailService(attribute)
        is CBCharacteristic -> detailCharacteristic(attribute)
        is CBDescriptor -> detailDescriptor(attribute)
        else -> detail("Unknown (Unrecognized)", attribute.toString())
    }
}
