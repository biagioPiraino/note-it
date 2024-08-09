export class UserData {
  id: string | undefined = "";
  name: string | undefined = "";
  email: string | undefined = "";
  nickname: string | undefined = "";
  connection: string | undefined = "";

  constructor(id: string | undefined,
              name: string | undefined,
              email: string | undefined,
              nickname: string | undefined){
    this.id = id;
    this.name = name;
    this.email = email;
    this.nickname = nickname;
    
    // Currently supporting only this type of authentication
    this.connection = "Username-Password-Authentication"
  }
}
