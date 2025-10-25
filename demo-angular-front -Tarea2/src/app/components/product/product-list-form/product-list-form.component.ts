import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormGroup, ReactiveFormsModule } from '@angular/forms';
import { IProductList } from '../../../interfaces';

@Component({
  selector: 'app-product-list-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule
  ],
  templateUrl: './product-list-form.component.html',
  styleUrl: './product-list-form.component.scss'
})
export class ProductListFormComponent {
  @Input() form!: FormGroup;
  @Output() callSaveMethod: EventEmitter<IProductList> = new EventEmitter<IProductList>();
}
