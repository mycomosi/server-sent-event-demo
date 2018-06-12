export class SseController {
    
	constructor() {
        console.log('new SeeControler');
    }

	connect(){
		this.evtSource = new EventSource("/api/sse");
		this.evtSource.onmessage = e => this._onMessage(e);
		this.evtSource.onerror = e => this._onerror(e);
		console.log('new SSE connexion established');
		
		this.evtSource.addEventListener("myEventType", e => this._onMyEvent(e), false);
	}
	
	_appendMessage(e){
		const newElement = document.createElement('li');
		const eventList = document.getElementById('list');
		
		const obj = JSON.parse(e.data);
		
		newElement.innerHTML = 'message: ' + obj.content;
		eventList.appendChild(newElement);
	}
	
	_onerror(e){
		console.log("EventSource failed.");
		//this.evtSource.close();
	}
	
	_onMyEvent(e){
		console.log('myEvent received');
		this._appendMessage(e);
	}
	
	_onMessage(e){
		console.log('Event with no type received');
		this._appendMessage(e);
	}

}