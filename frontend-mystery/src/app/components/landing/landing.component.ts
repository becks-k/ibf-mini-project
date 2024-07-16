import { Component, inject } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrl: './landing.component.css'
})
export class LandingComponent {

  private modalService = inject(NgbModal)

  openInstructions(content: any) {
    this.modalService.open(content, { size: 'lg', centered: true });
  }

}
