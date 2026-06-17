
import paho.mqtt.client as mqtt
import json, time, random, sys

interval = 2   # seconds between readings — can be changed by a command
sensor = sys.argv[1]

def on_message(client, userdata, msg):
    global interval
    try:
        cmd = json.loads(msg.payload.decode())   # parse the envelope {tid, method, data}
        tid = cmd["tid"]
        interval = int(cmd["data"])               # act on the command
        print("Command", tid, "— interval now", interval, "seconds")

        reply = json.dumps({"tid": tid, "result": "0"})   # echo SAME tid back
        client.publish("sensors/" + sensor + "/services_reply", reply)
    except Exception as e:
        print("Bad command:", e)

c = mqtt.Client()
c.connect("localhost", 1883)

c.on_message = on_message
c.subscribe("sensors/" + sensor + "/command")    # listen for commands
c.loop_start()

while True:
    payload = json.dumps({
        "sensorId": sensor,
        "temperature": round(random.uniform(18, 40), 1),
        "timestamp": time.strftime("%Y-%m-%d %H:%M:%S"),
        "timestampLong": int(time.time() * 1000)
    })
    c.publish("sensors/" + sensor + "/temperature", payload)
    print("sent", payload)
    time.sleep(interval)


