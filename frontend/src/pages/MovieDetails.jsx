import { useParams } from "react-router-dom";
import { Layout } from "../components/layout/Layout";

export function MovieDetails() {
    const { id } = useParams();

    return (
        <Layout>
            <div className="flex flex-col items-center justify-center min-h-[50vh] text-center">
                <h1 className="text-4xl font-bold mb-4">Movie Details</h1>
                <p className="text-xl text-muted-foreground">
                    Details for movie ID: <span className="text-primary font-mono">{id}</span>
                </p>
                <p className="mt-8 text-sm text-muted-foreground">
                    (This is a placeholder page. Real data fetching will be implemented next.)
                </p>
            </div>
        </Layout>
    );
}
