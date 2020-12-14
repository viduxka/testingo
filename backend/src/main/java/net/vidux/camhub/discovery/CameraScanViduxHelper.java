package net.vidux.camhub.discovery;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class CameraScanViduxHelper implements CameraScan {

  @Autowired private ViduxHelperWrapper viduxHelperWrapper;

  @Autowired private RawCameraDataFactory rawCameraDataFactory;

  @Override
  public CompletableFuture<Set<RawCameraData>> scanCams() {
    List<String> rawCameraLineList;
    try {
      rawCameraLineList = viduxHelperWrapper.findHikvisionIpCameras();
    } catch (IOException | TimeoutException e) {
      return CompletableFuture.failedFuture(e);
    }

    return CompletableFuture.completedFuture(
        rawCameraLineList.stream()
            .map(rawCameraDataFactory::createRawCameraData)
            .collect(Collectors.toCollection(HashSet::new)));
  }
}
