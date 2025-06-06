import { Component } from '@angular/core';

@Component({
  selector: 'app-inicio',
  imports: [],
  templateUrl: './inicio.component.html',
  styleUrl: './inicio.component.css'
})
export class InicioComponent {
  setActive(categoria: string){

    let btnContenedor = document.getElementById("filtro-categoria")
    let btns = btnContenedor?.getElementsByClassName("filtro-btn")
    // @ts-ignore
    for(let i = 0; i < btns.length; i++){
      if (btns) {
        if(btns[i].id == categoria){
          var currentActive = document.getElementsByClassName("active")
          currentActive[0].className = currentActive[0].className.replace(" active","")
          btns[i].className += " active"
        }
      }
    }
  }

}
