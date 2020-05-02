import { Component, OnInit } from "@angular/core";
import { TriviaService } from "./services/trivia.service";
import { forkJoin } from "rxjs";
import { CategoryModel } from "./models/category.model";
import { InformationModel } from "./models/information.model";

import { tap } from "rxjs/operators";
import { FormGroup, FormControl, Validators } from "@angular/forms";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.scss"]
})
export class AppComponent implements OnInit {
  public isDataLoaded = false;

  public categories: CategoryModel[] = [];
  public categoriesDictionary = new Map<number, CategoryModel>();
  public information: InformationModel[] = [];

  public currentInformation: FormGroup;
  public selectedId: number = null;

  public isShownSidePanel = false;

  constructor(private triviaService: TriviaService) {}

  public ngOnInit(): void {
    forkJoin([
      this.triviaService.getCategories(),
      this.triviaService.getUserAddedInformation()
    ])
      .pipe(
        tap(([categories, information]) => {
          this.categories = categories;
          this.information = information;

          this.categories.forEach(item =>
            this.categoriesDictionary.set(item.id, item)
          );
        })
      )
      .subscribe(() => (this.isDataLoaded = true));
  }

  public selectInformation(model: InformationModel): void {
    this.currentInformation = new FormGroup({
      id: new FormControl(model.id, Validators.required),
      categoryId: new FormControl(model.categoryId, Validators.required),
      title: new FormControl(model.title, Validators.required),
      text: new FormControl(model.text, Validators.required)
    });

    this.isShownSidePanel = true;
  }

  public closePanel(): void {
    this.isShownSidePanel = false;
    this.selectedId = null;
    this.currentInformation = null;
  }

  public updateInformation(): void {
    const model = this.currentInformation.getRawValue();

    if (model.categoryId || model.categoryId === 0) {
      this.triviaService.acceptUserAddedInformation(model).subscribe(() =>
        this.information.splice(
          this.information.findIndex(item => item.id === this.selectedId),
          1
        )
      );
    }

    this.isShownSidePanel = false;
    this.selectedId = null;
    this.currentInformation = null;
  }

  public deleteInformation(id: number): void {
    this.triviaService.deleteUserAddedInformation(id).subscribe(() =>
      this.information.splice(
        this.information.findIndex(item => item.id === id),
        1
      )
    );
  }
}
