package structural

class ContinuousDifferenceCalculator {
    fun calculateContinuous(start: Float, end: Float): Float = end - start
}

abstract class ComputesDiscreteDifference {
    abstract fun calculateDiscrete(start: Int, end: Int): Int
}

class ContinuousToDiscreteAdapter(
    private val calculator: ContinuousDifferenceCalculator
) : ComputesDiscreteDifference() {
    override fun calculateDiscrete(start: Int, end: Int): Int = calculator.run {
        calculateContinuous(start.toFloat(), end.toFloat()).toInt()
    }
}

abstract class ChargeableByLightning {
    abstract fun chargeWithLightning()
}

abstract class ChargeableByUsb {
    abstract fun chargeWithUsb()
}

class MacbookAir(private var batteryPercentage: Float) : ChargeableByUsb() {
    override fun chargeWithUsb() {
        batteryPercentage += 0.1f
    }
}

class IPhone(private var batteryPercentage: Float) : ChargeableByLightning() {
    override fun chargeWithLightning() {
        batteryPercentage += 0.1f
    }
}

abstract class Cord<T> {
    abstract fun connectTo(device: T)
}

class UsbCord : Cord<ChargeableByUsb>() {
    override fun connectTo(device: ChargeableByUsb) {
        device.chargeWithUsb()
    }
}

class LightningCord : Cord<ChargeableByLightning>() {
    override fun connectTo(device: ChargeableByLightning) {
        device.chargeWithLightning()
    }
}

class LightningToUsbAdapter(private val cord: LightningCord) : Cord<ChargeableByLightning>() {
    override fun connectTo(device: ChargeableByLightning) {
        cord.connectTo(device)
    }
}


fun main() {
    val continuousDifferenceCalculator = ContinuousDifferenceCalculator()
    println("Difference: " + continuousDifferenceCalculator.calculateContinuous(500f, 1000f))

    // Using the adapter, this operation can now be performed on ints
    // but use the same underlying calculation method
    val differenceAdapter = ContinuousToDiscreteAdapter(continuousDifferenceCalculator)
    println("Difference: " + differenceAdapter.calculateDiscrete(1, 2))

    val phone = IPhone(40f)
    val macbook = MacbookAir(10f)

    // OK, no need for an adapter
    val lightningCord = LightningCord()
    lightningCord.connectTo(phone)
    val usbCord = UsbCord()
    usbCord.connectTo(macbook)

    // We cannot do:
    // lightningCord.connectTo(macbook)
    // Our macbook has no lightning ports!
    // Solution: use an adapter
    val lightningAdapter = LightningToUsbAdapter(lightningCord)
    lightningAdapter.connectTo(phone)
}

