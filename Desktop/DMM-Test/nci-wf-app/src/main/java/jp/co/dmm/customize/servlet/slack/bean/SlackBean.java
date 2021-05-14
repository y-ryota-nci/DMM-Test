package jp.co.dmm.customize.servlet.slack.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class SlackBean {
	private String callback_id;
	private String corporation_code;
	private String process_id;
	private String user_added_info;
	private List<ActionsBean> actions;
	
	public String getCallback_id(){
		return callback_id;
	}
	
	public void setCallback_id(String callback_id){
		this.callback_id = callback_id;
	}

	public String getCorporation_code(){
		return corporation_code;
	}
	
	public void setCorporation_code(String corporation_code){
		this.corporation_code = corporation_code;
	}
	
	public String getProcess_id(){
		return process_id;
	}
	
	public void setProcess_id(String process_id){
		this.process_id = process_id;
	}
	
	public String getUser_added_info(){
		return user_added_info;
	}
	
	public void setUser_added_info(String user_added_info){
		this.user_added_info = user_added_info;
	}

	public List<ActionsBean> getActions(){
		return actions;
	}
	
	public void setActions(List<ActionsBean> actions){
		this.actions = actions;
	}
}
