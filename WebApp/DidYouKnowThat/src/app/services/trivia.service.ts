import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoryModel } from '../models/category.model';
import { HttpClient } from '@angular/common/http';
import { InformationModel } from '../models/information.model';

@Injectable({
    providedIn: 'root'
})
export class TriviaService {

    private baseUrl = 'https://cristiansarghe.com/api';

    constructor(
        private http: HttpClient
    ) { }

    public getCategories(): Observable<CategoryModel[]> {
        return this.http.get<CategoryModel[]>(`${this.baseUrl}/trivia/categories`);
    }

    public getUserAddedInformation(): Observable<InformationModel[]> {
        return this.http.get<InformationModel[]>(`${this.baseUrl}/moderatetrivia`);
    }

    public acceptUserAddedInformation(model: InformationModel): Observable<InformationModel> {
        return this.http.post<InformationModel>(`${this.baseUrl}/moderatetrivia`, model);
    }

    public deleteUserAddedInformation(id: number): Observable<void> {
        return this.http.delete<void>(`${this.baseUrl}/moderatetrivia/${id}`);
    }

}