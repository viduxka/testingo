package net.vidux.camhub.camera;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CameraRepository extends JpaRepository<Camera, String> {
  Camera findBySerialNumber(String serialNumber);
}
