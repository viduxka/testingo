package net.vidux.camhub.discovery;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CameraDiscoveryServiceTest {

  @Mock RawCameraEventPublisher rawCameraPublisher;

  @Mock DiscoveryTask discoveryTask;

  @Mock CompletableFuture<Set<RawCameraData>> mockFuture;

  @InjectMocks CameraDiscoveryService cameraDiscoveryService;

  @Test
  void requestDiscoverTestWhenScanIsRunning() {
    when(discoveryTask.discover()).thenReturn(mockFuture);
    Assertions.assertDoesNotThrow(
        cameraDiscoveryService::requestDiscovery,
        "Unexpected exception thrown while requestingDiscovery");
    Assertions.assertThrows(
        CameraDiscoveryException.class,
        cameraDiscoveryService::requestDiscovery,
        "No DiscoveryException thrown when a discovery service is already running");
  }

  @Test
  void requestDiscoverTestWhenNoScanIsRunning() {
    Set<RawCameraData> mockScanResultSet = new HashSet<>();
    RawCameraData cam1 = mock(RawCameraData.class);
    RawCameraData cam2 = mock(RawCameraData.class);
    RawCameraData cam3 = mock(RawCameraData.class);
    mockScanResultSet.add(cam1);
    mockScanResultSet.add(cam2);
    mockScanResultSet.add(cam3);
    when(discoveryTask.discover()).thenReturn(CompletableFuture.completedFuture(mockScanResultSet));

    Assertions.assertDoesNotThrow(
        cameraDiscoveryService::requestDiscovery,
        "Unexpected exception thrown while requestingDiscovery");
    verify(rawCameraPublisher, times(1)).publishRawCameraEvent(cam1);
    verify(rawCameraPublisher, times(1)).publishRawCameraEvent(cam2);
    verify(rawCameraPublisher, times(1)).publishRawCameraEvent(cam3);
  }
}
