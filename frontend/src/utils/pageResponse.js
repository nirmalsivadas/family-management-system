export function normalizePageResponse(pageData) {
  const content = pageData?.content ?? (Array.isArray(pageData) ? pageData : []);
  const page = pageData?.page ?? {};

  return {
    content,
    totalElements: pageData?.totalElements ?? page.totalElements ?? content.length,
    totalPages: pageData?.totalPages ?? page.totalPages ?? 1,
  };
}
