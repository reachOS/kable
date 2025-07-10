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

private fun LogMessage.detail(service: CBService) {
    detail("service", service.UUID.UUIDString)
}

private fun LogMessage.detail(characteristic: CBCharacteristic) {
    detail(
        characteristic.service!!.UUID.toUuid(),
        characteristic.UUID.toUuid(),
    )
}

private fun LogMessage.detail(descriptor: CBDescriptor) {
    detail(
        descriptor.characteristic!!.service!!.UUID.toUuid(),
        descriptor.characteristic!!.UUID.toUuid(),
        descriptor.UUID.toUuid(),
    )
}

internal fun LogMessage.detail(attribute: CBAttribute) {
    when (attribute) {
        is CBService -> detail(attribute)
        is CBCharacteristic -> detail(attribute)
        is CBDescriptor -> detail(attribute)
        else -> detail("unknown", attribute.UUID.toUuid())
    }
}
