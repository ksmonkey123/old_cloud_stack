package ch.awae.cloud.netcode;

import java.sql.Timestamp;

import lombok.Getter;

@Getter
public class Message {
	private String user;
	private Timestamp time;
	private byte[] body;
}
