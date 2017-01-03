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
        input "internal_method","enum", title: "Internal Method (Defaults to GET)", required: false, options: ["GET","POST","PUT"]
		input "input_on_key_1","text", title: "On Key 1", required: false
        input "input_on_value_1","text", title: "On Value 1", required: false
		input "input_off_key_1","text", title: "Off Key 1", required: false
        input "input_off_value_1","text", title: "Off Value 1", required: false
		input "input_on_key_2","text", title: "On Key 2", required: false
        input "input_on_value_2","text", title: "On Value 2", required: false
		input "input_off_key_2","text", title: "Off Key 2", required: false
        input "input_off_value_2","text", title: "Off Value 2", required: false
		input "input_on_key_3","text", title: "On Key 3", required: false
        input "input_on_value_3","text", title: "On Value 3", required: false
		input "input_off_key_3","text", title: "Off Key 3", required: false
        input "input_off_value_3","text", title: "Off Value 3", required: false
		input "input_on_key_4","text", title: "On Key 4", required: false
        input "input_on_value_4","text", title: "On Value 4", required: false
		input "input_off_key_4","text", title: "Off Key 4", required: false
        input "input_off_value_4","text", title: "Off Value 4", required: false
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

def internal_method = internal_method ? internal_method : "GET"

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
        def myGroovyMap = ""
        if (input_on_key_1){
        	myGroovyMap += "${input_on_key_1}=${input_on_value_1}"
        }
        if (input_on_key_2){
        	myGroovyMap += "&${input_on_key_2}=${input_on_value_2}"
        }
        if (input_on_key_3){
        	myGroovyMap += "&${input_on_key_3}=${input_on_value_3}"
        }
        if (input_on_key_4){
        	myGroovyMap += "&${input_on_key_4}=${input_on_value_4}"
        }
        if (myGroovyMap == "" && input_on_value_1 != ""){
        	myGroovyMap += input_on_value_1
        }
        def json = new groovy.json.JsonBuilder(myGroovyMap)
        log.debug json

		def result = new physicalgraph.device.HubAction(
				method: "${internal_method}",
				path: "${internal_on_path}",
				headers: [
					HOST: "${internal_ip}:${port}",
                    "Content-Type": "application/x-www-form-urlencoded"
				],
                body: myGroovyMap
        )
        sendHubCommand(result)
        sendEvent(name: "switch", value: "on") 
        log.debug "result" + result
        log.debug "Executing ON" 

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
        def myGroovyMap = ""
        if (input_off_key_1){
        	myGroovyMap += "${input_off_key_1}=${input_off_value_1}"
        }
        if (input_off_key_2){
        	myGroovyMap += "&${input_off_key_2}=${input_off_value_2}"
        }
        if (input_off_key_3){
        	myGroovyMap += "&${input_off_key_3}=${input_off_value_3}"
        }
        if (input_off_key_4){
        	myGroovyMap += "&${input_off_key_4}=${input_off_value_4}"
        }
        if (myGroovyMap == "" && input_off_value_1 != ""){
        	myGroovyMap += input_off_value_1
        }

		def result = new physicalgraph.device.HubAction(
				method: "${internal_method}",
				path: "${internal_off_path}",
				headers: [
					HOST: "${internal_ip}:${port}",
                    "Content-Type": "application/x-www-form-urlencoded"
				],
                body: myGroovyMap

        )

			sendHubCommand(result)
			sendEvent(name: "switch", value: "off")
			log.debug "Executing OFF" 
			log.debug result
	}
}