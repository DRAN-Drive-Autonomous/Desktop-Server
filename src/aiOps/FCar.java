package aiOps;

public class FCar extends PIDController {
	public String name;
	public String owner;
	
	public FCar() {
		super(0.5f, 0.05f, 0.01f);
		this.name = "Bravado Buffalo S";
		this.owner = "Franklin Clintor";
	}
}
