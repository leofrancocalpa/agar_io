function login(){
    var username= document.getElementsByClassName("input")[0].value;
    var password= document.getElementsByClassName("input")[1].value;
    var mostrar= new XMLHttpRequest();
    mostrar.send(username);

}

function validarTexto(){
var text= document.getElementsByClassName("input")[0].value;
return text;
alert(text)
}

function verificarUsuario(){
var arrayData= new Array();
var archivoTxt= new XMLHttpRequest();
var fileRuta= 'users.txt';
archivoTxt.open("GET",fileRuta,false);
archivoTxt.send(null);
var txt = archivoTxt.responseText;
var cedula = validarTexto();
var count=0;
var x= false;
var p ="";


    for (var i=0;  txt[i]!="-" &&!x ; i++){

        

        for(var j=i*7; j<(i*7)+7;j++){
            p=p.concat(txt[j]);
        }

        
        
        if(p==cedula){
            x=true;
        }
    
        p="";
        
    }
if(x){
    alert(x);
    seRegistro(cedula);
}

}

function seRegistro(var Username){

var mostrar= new XMLHttpRequest();
mostrar.open("GET",Username,false);
mostrar.send(Username);

}