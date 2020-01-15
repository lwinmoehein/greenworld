package trapleh.io.greenworld.model;

public class Plant {
    private String id,name,address,status,date;
    public Plant (String id,String name,String address,String status,String date){
        this.id=id;
        this.name=name;
        this.address=address;
        this.status=status;
        this.date=date;
    }
    public Plant(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
