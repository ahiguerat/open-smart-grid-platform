package org.opensmartgridplatform.adapter.protocol.oslp.elster.application.services;

import org.opensmartgridplatform.adapter.ws.shared.db.domain.repositories.writable.WritableDeviceRepository;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.valueobjects.DeviceLifecycleStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "ormDeviceService")
@Transactional(value = "writableTransactionManager")
public class OrmDeviceService {
	
	@Autowired
    private WritableDeviceRepository writableDeviceRepository;
	
	public OrmDeviceService() {
        // Parameterless constructor required for transactions...
    }
	
	public Device updateDeviceLifecycleStatus(String deviceIdentification, DeviceLifecycleStatus status) {
		Device device = this.writableDeviceRepository.findByDeviceIdentification(deviceIdentification);
		device.setDeviceLifecycleStatus(status);
		
		return this.writableDeviceRepository.saveAndFlush(device);
	}
}
