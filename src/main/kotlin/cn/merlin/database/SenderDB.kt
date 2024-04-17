package cn.merlin.database

import cn.merlin.bean.Device
import cn.merlin.bean.Message
import cn.merlin.database.model.DeviceModel
import cn.merlin.database.model.DeviceModel.deviceId
import cn.merlin.database.model.DeviceModel.deviceIdentifier
import cn.merlin.database.model.DeviceModel.deviceIpAddress
import cn.merlin.database.model.DeviceModel.deviceName
import cn.merlin.database.model.DeviceModel.deviceNickName
import cn.merlin.database.model.DeviceModel.deviceType
import cn.merlin.database.model.MessageModel
import cn.merlin.utils.databasePath
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class SenderDB {
    val database = Database.connect("jdbc:sqlite:${databasePath.value}", driver = "org.sqlite.JDBC")
    fun createTables() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(DeviceModel)
            SchemaUtils.create(MessageModel)
        }
    }

    fun insertDevice(device: cn.merlin.bean.model.DeviceModel): Int {
        var deviceId: Int = -1
        transaction {
            deviceId = DeviceModel.insert {
                it[deviceName] = device.deviceName.value
                it[deviceIpAddress] = device.deviceIpAddress.value
                it[deviceNickName] = device.deviceName.value
                it[deviceIdentifier] = device.deviceIdentifier.value
                it[deviceType] = device.deviceType.value
            } get DeviceModel.deviceId
        }
        return deviceId
    }

    fun selectAllDevice(): MutableList<Device> {
        val devices: MutableList<Device> = mutableListOf()
        transaction {
            val query = DeviceModel.selectAll()
            query.forEach {
                val device = Device()
                device.deviceId = it[deviceId]
                device.deviceIpAddress = it[deviceIpAddress]
                device.deviceName = it[deviceName]
                device.deviceNickName = it[deviceNickName]
                device.deviceIdentifier = it[deviceIdentifier]
                device.deviceType = it[deviceType]
                devices.add(device)
            }
        }
        return devices
    }

    fun selectDeviceById(deviceId: Int): Device {
        val device = Device()
        transaction {
            val query = DeviceModel.select(DeviceModel.deviceId).where { DeviceModel.deviceId eq deviceId }
            query.forEach {
                device.deviceIpAddress = it[deviceIpAddress]
                device.deviceName = it[deviceName]
                device.deviceNickName = it[deviceNickName]
                device.deviceIdentifier = it[deviceIdentifier]
                device.deviceType = it[deviceType]
            }
        }
        return device
    }

    fun deleteDeviceById(deviceId: Int): Int {
        var result: Int = -1
        transaction {
            result = DeviceModel.deleteWhere { DeviceModel.deviceId eq deviceId }
        }
        return result
    }

    fun insertMessage(message: cn.merlin.bean.model.MessageModel): Int {
        var messageId: Int = -1
        transaction {
            messageId = MessageModel.insert {
                it[messageType] = message.messageType.value
                it[messageContent] = message.messageContent.value
                it[messageSenderIpAddress] = message.messageSenderIpAddress.value
                it[messageReceiverIpAddress] = message.messageReceiverIpAddress.value
                it[messageSenderIdentifier] = message.messageSenderIdentifier.value
                it[messageReceiverIdentifier] = message.messageReceiverIdentifier.value
            } get MessageModel.messageId
        }
        return messageId
    }

    fun selectAllMessage(): MutableList<Message> {
        val messages: MutableList<Message> = mutableListOf()
        transaction {
            val query = MessageModel.selectAll()
            query.forEach {
                val message = Message(
                    it[MessageModel.messageId],
                    it[MessageModel.messageType],
                    it[MessageModel.messageContent],
                    it[MessageModel.messageSenderIpAddress],
                    it[MessageModel.messageReceiverIpAddress],
                    it[MessageModel.messageSenderIdentifier],
                    it[MessageModel.messageReceiverIdentifier]
                )
                messages.add(message)
            }
        }
        return messages
    }

    fun selectMessageById(messageId: Int): Query? {
        var query: Query? = null
        transaction {
            query = MessageModel.select(MessageModel.messageId).where { MessageModel.messageId eq messageId }
        }
        return query
    }

    fun deleteMessageById(messageId: Int): Int {
        var result: Int = -1
        transaction {
            result = MessageModel.deleteWhere { MessageModel.messageId eq messageId }
        }
        return result
    }
}

