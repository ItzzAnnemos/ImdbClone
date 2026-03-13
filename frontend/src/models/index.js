/**
 * Barrel export — import all models from a single location:
 *
 *   import * as MediaModel  from "../models";            // default re-export keys
 *   import * as MediaModel  from "../models/media.model";
 *
 * Or pick named exports:
 *   import { fromApi as mediaFromApi, fromApiList as mediaFromApiList } from "../models/media.model";
 */

export * as MediaModel   from "./media.model";
export * as MovieModel   from "./movie.model";
export * as TVSeriesModel from "./tvSeries.model";
export * as UserModel    from "./user.model";
export * as PersonModel  from "./person.model";
export * as GenreModel   from "./genre.model";
export * as RatingModel  from "./rating.model";
export * as ReviewModel  from "./review.model";
