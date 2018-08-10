package ch.awae.cloud.netcode;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ch.awae.netcode.NetcodeServerFactory;

@Service
public class NetcodeService {

	@Autowired
	public NetcodeService(AppRepo repo) throws IOException {
		System.out.println("starting");
		NetcodeServerFactory nsf = new NetcodeServerFactory(7777);
		nsf.setAppIdValidator(i -> repo.findByIdentifier(i).isPresent());
		nsf.setMaxClients(256);
		nsf.start();
	}

}
