# publisher.py
import paho.mqtt.client as mqtt, json, time, random
c = mqtt.Client()
c.connect("localhost", 1883)
while True:
    payload = json.dumps({"sensorId":"sensor-1","temperature":round(random.uniform(18,30),1),"timestamp":int(time.time()*1000)})
    c.publish("sensors/sensor-1/temperature", payload)
    print("sent", payload)
    time.sleep(2)