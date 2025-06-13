function toggleChat() {
    const widget = document.getElementById('chatWidget');
    const openBtn = document.getElementById('openChatBtn');
    if (widget.style.display === 'none' || widget.style.display === '') {
        widget.style.display = 'block';
        openBtn.style.display = 'none';
    } else {
        widget.style.display = 'none';
        openBtn.style.display = 'flex';
    }
}

let stompClient = null;
function connect() {
    const socket = new SockJS('/ws-chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/messages', function (messageOutput) {
            showMessage(JSON.parse(messageOutput.body));
        });
    });
}
function showMessage(message) {
    const chatMessages = document.getElementById('chatMessages');
    const msgDiv = document.createElement('div');
    msgDiv.innerHTML = `<b>${message.sender}:</b> ${message.content}`;
    chatMessages.appendChild(msgDiv);
    chatMessages.scrollTop = chatMessages.scrollHeight;
}
document.addEventListener('DOMContentLoaded', function () {
    connect();
    document.getElementById('sendBtn').onclick = function () {
        const input = document.getElementById('chatInput');
        const text = input.value.trim();
        if (text && stompClient) {
            stompClient.send("/app/chat.send", {}, JSON.stringify({'sender': 'Пользователь', 'content': text}));
            input.value = '';
        }
    };
});