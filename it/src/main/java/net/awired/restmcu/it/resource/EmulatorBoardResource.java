package net.awired.restmcu.it.resource;

public class EmulatorBoardResource extends LatchBoardResource {

    public EmulatorBoardResource(String ip, int port) {
        super(ip + ':' + port);
        boardSettings.setIp(ip);
        boardSettings.setPort(port);
    }

}
