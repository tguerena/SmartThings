/*
* Author: tguerena and surge919
*
* Device Handler
*/


preferences {
	section("External Access"){
		input "external_on_uri", "text", title: "External On URI", required: false
		input "external_off_uri", "text", title: "External Off URI", required: false
	}
    
	section("Internal Access"){
		input "internal_ip", "text", title: "Internal IP", required: false
		input "internal_port", "text", title: "Internal Port (if not 80)", required: false
		input "internal_on_path", "text", title: "Internal On Path (/blah?q=this)", required: false
		input "internal_off_path", "text", title: "Internal Off Path (/blah?q=this)", required: false
	}
}




metadata {
	definition (name: "URI Switch", namespace: "tguerena", author: "Troy Guerena") {
		capability "Actuator"
			capability "Switch"
			capability "Sensor"
	}

	// simulator metadata
	simulator {
	}

	// UI tile definitions
	tiles {
		standardTile("button", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "off", label: 'Off', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff", nextState: "on"
				state "on", label: 'On', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821", nextState: "off"
		}
		standardTile("offButton", "device.button", width: 1, height: 1, canChangeIcon: true) {
			state "default", label: 'Force Off', action: "switch.off", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
		standardTile("onButton", "device.switch", width: 1, height: 1, canChangeIcon: true) {
			state "default", label: 'Force On', action: "switch.on", icon: "st.switches.switch.on", backgroundColor: "#79b821"
		}
		main "button"
			details (["button","onButton","offButton"])
	}
}

def parse(String description) {
	log.debug(description)
}

def on() {
	if (external_on_uri){
		// sendEvent(name: "switch", value: "on")
		// log.debug "Executing ON"

		def cmd = "${settings.external_on_uri}";

		log.debug "Sending request cmd[${cmd}]"

			httpGet(cmd) {resp ->
				if (resp.data) {
					log.info "${resp.data}"
				} 
			}
	}
	if (internal_on_path){
		def port
			if (internal_port){
				port = "${internal_port}"
			} else {
				port = 80
			}

		def result = new physicalgraph.device.HubAction(
				method: "GET",
				path: "${internal_on_path}",
				headers: [
				HOST: "${internal_ip}:${port}"
				]
				)
			sendHubCommand(result)
			sendEvent(name: "switch", value: "on") 
			log.debug "Executing ON" 
			log.debug result
	}
}

def off() {
	if (external_off_uri){
		def cmd = "${settings.external_off_uri}";
		log.debug "Sending request cmd[${cmd}]"
			httpGet(cmd) {resp ->
				if (resp.data) {
					log.info "${resp.data}"
				} 
			}
	}
	if (internal_off_path){
		def port
			if (internal_port){
				port = "${internal_port}"
			} else {
				port = 80
			}

		def result = new physicalgraph.device.HubAction(
				method: "GET",
				path: "${internal_off_path}",
				headers: [
				HOST: "${internal_ip}:${port}"
				]
				)

			sendHubCommand(result)
			sendEvent(name: "switch", value: "off")
			log.debug "Executing OFF" 
			log.debug result
	}
}
