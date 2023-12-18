package aiOps;

class PIDController {
	protected float Kp;
	protected float Kd;
	protected float Ki;
	protected int delta = 1;
    protected float prev_speed_error = 0;
    protected float accu_speed_error=0.0f;
	
	public float target_speed;
	
	public PIDController(float value1, float value2, float value3) {
		this.Kp = value1;
		this.Kd = value2;
		this.Ki = value3;
	}
	
	public void setTargetSpeed(float value) {
		this.target_speed = value;
	}
	
	public float getTargetSpeed() {
		return this.target_speed;
	}
	
	protected void incrementDelta() {
		this.delta += 1;
	}
	
	protected void resetPID() {
		this.delta = 1;
		this.prev_speed_error = 0;
		this.accu_speed_error = 0.0f;
	}
	
	protected void setPreviousSpeedError(float value) {
		this.prev_speed_error = value;
	}
	
	protected void initiatePreviousSpeedError() {
		this.setPreviousSpeedError(this.target_speed);
	}
	
	public String sendPIDAction(float value) {
        float KPH = value;
        float kph_error;
        float speed_error_der;
        float pid_value;
        kph_error = this.target_speed - KPH;
        speed_error_der = (kph_error - this.prev_speed_error) / this.delta;
        this.setPreviousSpeedError(kph_error);
        this.accu_speed_error += kph_error * this.delta;
        pid_value = (this.Kp * kph_error) + (this.Kd * speed_error_der) + (this.Ki * this.accu_speed_error);
        this.incrementDelta();
        
	    if (pid_value >= 1){
		    return "W";
	    }else if(pid_value <= -1){
		    return "S";
	    } else {
		    return "N";
	    }
	}
	
	public void initiatePID(float value) {
		this.setTargetSpeed(value);
		this.resetPID();
		this.initiatePreviousSpeedError();
	}
}