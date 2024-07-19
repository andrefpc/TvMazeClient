package com.andrefpc.tvmazeclient.view_model.main
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.biometric.BiometricManager
import androidx.fragment.app.FragmentActivity
import com.andrefpc.tvmazeclient.R
import com.andrefpc.tvmazeclient.domain.repository.preferences.PinRepository
import com.andrefpc.tvmazeclient.presentation.compose.screen.main.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.coVerify
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var pinRepository: PinRepository

    private lateinit var viewModel: MainViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxed = true)

        Dispatchers.setMain(testDispatcher)

        viewModel = MainViewModel(pinRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // reset the main dispatcher to the original Main dispatcher
    }

    @Test
    fun `checkPin should update button state correctly`() = runTest {
        // Given
        every { pinRepository.getPin() } returns null

        // When
        viewModel.checkPin()
        advanceUntilIdle()

        // Then
        assertEquals(R.string.create_button, viewModel.pinButtonState.value)

        // Given
        every { pinRepository.getPin() } returns "1234"

        // When
        viewModel.checkPin()
        advanceUntilIdle()

        // Then
        assertEquals(R.string.login_button, viewModel.pinButtonState.value)
    }

    @Test
    fun `onPinClick should handle empty pin correctly`() = runTest {
        // Given
        val context = mockk<Context>(relaxed = true)
        every { context.getString(R.string.empty_pin) } returns "Empty PIN"

        // When
        viewModel.onPinClick(context, "")

        // Then
        assertEquals("Empty PIN", viewModel.showMessage.first())
    }

    @Test
    fun `onPinClick should save new pin and update state correctly`() = runTest {
        // Given
        val context = mockk<Context>(relaxed = true)
        every { pinRepository.getPin() } returns null

        // When
        viewModel.onPinClick(context, "1234")

        // Then
        coVerify { pinRepository.savePin("1234") }
        assertEquals(R.string.login_button, viewModel.pinButtonState.value)
        assertEquals(Unit, viewModel.openShowScreen.first())
    }

    @Test
    fun `onPinClick should open shows if pin matches`() = runTest {
        // Given
        val context = mockk<Context>(relaxed = true)
        every { pinRepository.getPin() } returns "1234"

        // When
        viewModel.onPinClick(context, "1234")

        // Then
        assertEquals(Unit, viewModel.openShowScreen.first())
    }

    @Test
    fun `onPinClick should show error message if pin does not match`() = runTest {
        // Given
        val context = mockk<Context>(relaxed = true)
        every { context.getString(R.string.incorrect_pin) } returns "Incorrect PIN"
        every { pinRepository.getPin() } returns "1234"

        // When
        viewModel.onPinClick(context, "5678")

        // Then
        assertEquals("Incorrect PIN", viewModel.showMessage.first())
    }

    @Test
    fun `verifyBiometrics should check and initialize biometrics correctly`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_SUCCESS

        // When
        viewModel.verifyBiometrics(context)

        // Then
        assertEquals(Unit, viewModel.initBiometrics.first())
    }

    @Test
    fun `checkBiometrics should return true and initialize biometrics on success`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_SUCCESS

        // When
        val result = viewModel.checkBiometrics(context)

        // Then
        assert(result)
    }

    @Test
    fun `checkBiometrics should return false and show message on BIOMETRIC_ERROR_NONE_ENROLLED`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED
        every { context.getString(R.string.biometric_enrolled) } returns "Please enroll biometrics"

        // When
        val result = viewModel.checkBiometrics(context)

        // Then
        assert(!result)
        assertEquals("Please enroll biometrics", viewModel.showMessage.first())
    }

    @Test
    fun `checkBiometrics should return false and show message on BIOMETRIC_ERROR_NO_HARDWARE`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE
        every { context.getString(R.string.no_biometric_available) } returns "No biometric hardware available"

        // When
        val result = viewModel.checkBiometrics(context)

        // Then
        assert(!result)
        assertEquals("No biometric hardware available", viewModel.showMessage.first())
    }

    @Test
    fun `checkBiometrics should return false and show message on BIOMETRIC_ERROR_HW_UNAVAILABLE`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE
        every { context.getString(R.string.biometric_unavailable) } returns "Biometric hardware unavailable"

        // When
        val result = viewModel.checkBiometrics(context)

        // Then
        assert(!result)
        assertEquals("Biometric hardware unavailable", viewModel.showMessage.first())
    }

    @Test
    fun `checkBiometrics should return false and show message on BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED
        every { context.getString(R.string.no_biometric_available) } returns "Security update required"

        // When
        val result = viewModel.checkBiometrics(context)

        // Then
        assert(!result)
        assertEquals("Security update required", viewModel.showMessage.first())
    }

    @Test
    fun `checkBiometrics should return false and show message on BIOMETRIC_ERROR_UNSUPPORTED`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED
        every { context.getString(R.string.biometric_unsupported) } returns "Biometric unsupported"

        // When
        val result = viewModel.checkBiometrics(context)

        // Then
        assert(!result)
        assertEquals("Biometric unsupported", viewModel.showMessage.first())
    }

    @Test
    fun `checkBiometrics should return false and show message on BIOMETRIC_STATUS_UNKNOWN`() = runTest {
        // Given
        val context = mockk<FragmentActivity>(relaxed = true)
        val biometricManager = mockk<BiometricManager>()
        mockkStatic(BiometricManager::class)
        every { BiometricManager.from(context) } returns biometricManager
        every { biometricManager.canAuthenticate(any()) } returns BiometricManager.BIOMETRIC_STATUS_UNKNOWN
        every { context.getString(R.string.cant_authenticate_with_biometrics) } returns "Can't authenticate with biometrics"

        // When
        val result = viewModel.checkBiometrics(context)

        // Then
        assert(!result)
        assertEquals("Can't authenticate with biometrics", viewModel.showMessage.first())
    }

    @Test
    fun `onBiometricsClick should open biometrics authentication if biometrics are availablee`() = runTest {
        // Given
        viewModel.canUseBiometrics = true

        // When
        viewModel.onBiometricsClick()

        // Then
        assertEquals(Unit, viewModel.showBiometricsAuthentication.first())
    }

    @Test
    fun `onBiometricsClick should open phone authentication if biometrics are not available`() = runTest {
        // Given
        viewModel.canUseBiometrics = false

        // When
        viewModel.onBiometricsClick()

        // Then
        assertEquals(Unit, viewModel.showPhoneAuthentication.first())
    }

    @Test
    fun `sendMessage should emit the correct message`() = runTest {
        // Given
        val message = "Test Message"

        // When
        viewModel.sendMessage(message)

        // Then
        assertEquals(message, viewModel.showMessage.first())
    }

    @Test
    fun `openShows should emit the correct signal`() = runTest {
        // When
        viewModel.openShows()

        // Then
        assertEquals(Unit, viewModel.openShowScreen.first())
    }
}