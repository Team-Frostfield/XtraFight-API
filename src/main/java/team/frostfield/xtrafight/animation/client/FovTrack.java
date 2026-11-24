 package team.frostfield.xtrafight.animation.client;

public class FovTrack {

    private final float[] times;
    private final float[] values;

    public FovTrack(float[] times, float[] values) {
        if(times.length == 0 || times.length != values.length){
            throw new IllegalArgumentException("Invalid FOV track in the JSON");
        }

        this.times = times;
        this.values = values;
    }

    public float getFovFloatAtTimeWithInterp(float time) {
        if(time <= times[0]) return values[0];
        int last = times.length -1;
        if(time >= times[last]) return values[last];
        for(int i = 0; i < last; i++){
            if(time >= times[1] && time <= times[i +1]){
            float a = (time - times[i] / (times[i+1] - times[i]));
            return values[i] + a * (values[i + 1] - values[i]);
            
            }
        }
        return values[last];
    }

    public float getFovFloatAtTime(float time){
        for(int i =  times.length -1; i >= 0; i--){
            if(time >= times[i]){
                return values[i];
            }
        }
        return values[0];
    }
    
}