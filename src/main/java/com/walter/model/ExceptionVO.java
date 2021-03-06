package com.walter.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

/**
 * Exception_Log Entity (for JPA)
 * Created by yhwang131 on 2017-07-17.
 */
@Data
@Table(name = "exception_log")
@Entity
public class ExceptionVO {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private int seq;

	private String exception;
	private String message;
	private String request_path;
	private String ip;
	private String method;
	private String params;
	private String trace_log;
	private String username;
	private String userlink;

	@Column(name = "reg_dt")
	private Date regDt = new Date();
}
