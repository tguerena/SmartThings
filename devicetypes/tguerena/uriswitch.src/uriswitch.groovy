/*
* Author: Troy Guerena and Tom Valdes
*/


preferences {
	input("on_uri", "text", title: "On URI", required: false)
		input("off_uri", "text", title: "Off URI", required: false)
		input("local_ip", "text", title: "Local IP", required: false)
		input("local_port", "text", title: "Local Port (if not 80)", required: false)
		input("local_off_path", "text", title: "Local Off Path (/blah?q=this)", required: false)
		input("local_on_path", "text", title: "Local On Path (/blah?q=this)", required: false)
}
metadata {
	definition (name: "On & Off Button Tiles as Defined from URIs", namespace: "smartthings", author: "SmartThings") {
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
	if (on_uri){
		// sendEvent(name: "switch", value: "on")
		// log.debug "Executing ON"

		def cmd = "${settings.on_uri}";

		log.debug "Sending request cmd[${cmd}]"

			httpGet(cmd) {resp ->
				if (resp.data) {
					log.info "${resp.data}"
				} 
			}
	}
	if (local_on_path){
		def port
			if (local_port){
				port = "${local_port}"
			} else {
				port = 80
			}

		def result = new physicalgraph.device.HubAction(
				method: "GET",
				path: "${local_on_path}",
				headers: [
				HOST: "${local_ip}:${port}"
				]
				)
			sendHubCommand(result)
			sendEvent(name: "switch", value: "on") 
			log.debug "Executing ON" 
			log.debug result
	}
}

def off() {
	if (off_uri){
		// sendEvent(name: "switch", value: "off")
		// log.debug "Executing OFF"

		def cmd = "${settings.off_uri}";

		log.debug "Sending request cmd[${cmd}]"

			httpGet(cmd) {resp ->
				if (resp.data) {
					log.info "${resp.data}"
				} 
			}
	}
	if (local_off_path){
		def port
			if (local_port){
				port = "${local_port}"
			} else {
				port = 80
			}

		def result = new physicalgraph.device.HubAction(
				method: "GET",
				path: "${local_off_path}",
				headers: [
				HOST: "${local_ip}:${port}"
				]
				)

			sendHubCommand(result)
			sendEvent(name: "switch", value: "off")
			log.debug "Executing OFF" 
			log.debug result
	}
}
