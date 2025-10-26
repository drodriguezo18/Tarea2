import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ICategoryList } from '../../../interfaces';

@Component({
  selector: 'app-category-list',
  standalone: true,
  imports: [],
  templateUrl: './category-list.component.html',
  styleUrl: './category-list.component.scss'
})
export class CategoryListComponent {
  @Input() categoriesList: ICategoryList[] = [];
  @Output() callEditMethod: EventEmitter<ICategoryList> = new EventEmitter<ICategoryList>();
  @Output() callDeleteMethod: EventEmitter<ICategoryList> = new EventEmitter<ICategoryList>();
}
