import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { ArrowLeft, Calendar, UserRound } from "lucide-react";
import { Layout } from "../components/layout/Layout";
import { Button } from "../components/ui/Button";
import { getPersonById } from "../lib/personService";

function formatDate(date) {
    if (!date) {
        return "Birth date unavailable";
    }

    return new Intl.DateTimeFormat("en", {
        year: "numeric",
        month: "long",
        day: "numeric",
    }).format(new Date(date));
}

export function PersonDetails() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [person, setPerson] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchPerson = useCallback(async () => {
        try {
            setLoading(true);
            setError(null);
            setPerson(await getPersonById(id));
        } catch (err) {
            console.error("Failed to load person:", err);
            setError("Failed to load this person. Please try again.");
        } finally {
            setLoading(false);
        }
    }, [id]);

    useEffect(() => {
        fetchPerson();
    }, [fetchPerson]);

    const handleBack = () => {
        if (window.history.length > 1) {
            navigate(-1);
            return;
        }

        navigate("/");
    };

    return (
        <Layout>
            <div className="mb-6">
                <Button variant="ghost" size="sm" className="gap-2" onClick={handleBack}>
                    <ArrowLeft className="h-4 w-4" />
                    Back
                </Button>
            </div>

            {loading && <p className="text-sm text-muted-foreground">Loading person...</p>}

            {!loading && error && (
                <div className="rounded-md border border-destructive/30 bg-destructive/10 p-4">
                    <p className="mb-3 text-sm text-destructive">{error}</p>
                    <Button variant="outline" size="sm" onClick={fetchPerson}>
                        Retry
                    </Button>
                </div>
            )}

            {!loading && !error && person && (
                <section className="grid gap-8 md:grid-cols-[220px_minmax(0,1fr)]">
                    <div className="overflow-hidden rounded-md border border-border bg-secondary">
                        {person.profilePictureUrl ? (
                            <img
                                src={person.profilePictureUrl}
                                alt={person.fullName}
                                className="aspect-[2/3] w-full object-cover"
                            />
                        ) : (
                            <div className="flex aspect-[2/3] items-center justify-center">
                                <UserRound className="h-16 w-16 text-muted-foreground" />
                            </div>
                        )}
                    </div>

                    <div className="min-w-0">
                        <p className="mb-2 text-xs font-semibold uppercase text-yellow-500">
                            Person
                        </p>
                        <h1 className="text-3xl font-bold tracking-tight md:text-4xl">
                            {person.fullName}
                        </h1>
                        <div className="mt-4 flex items-center gap-2 text-sm text-muted-foreground">
                            <Calendar className="h-4 w-4" />
                            <span>{formatDate(person.birthDate)}</span>
                        </div>
                        <div className="mt-8 max-w-3xl">
                            <h2 className="mb-3 text-lg font-semibold">Biography</h2>
                            <p className="leading-7 text-muted-foreground">
                                {person.biography || "No biography is available yet."}
                            </p>
                        </div>
                        {person.mediaCredits && person.mediaCredits.length > 0 && (
                            <div className="mt-8 max-w-3xl">
                                <h2 className="mb-3 text-lg font-semibold">Known For</h2>
                                <ul className="space-y-2">
                                    {person.mediaCredits.map((credit, i) => (
                                        <li
                                            key={i}
                                            className="flex items-center gap-2 text-sm text-muted-foreground"
                                        >
                                            <span className="font-medium text-foreground">
                                                {credit.mediaTitle}
                                            </span>
                                            <span>·</span>
                                            <span className="capitalize lowercase">
                                                {credit.role}
                                            </span>
                                            {credit.characterName && (
                                                <span>
                                                    as <em>{credit.characterName}</em>
                                                </span>
                                            )}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        )}
                    </div>
                </section>
            )}
        </Layout>
    );
}
