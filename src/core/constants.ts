const production = import.meta.env.PROD;
const platform = import.meta.env.VITE_RUNTIME_PLATFORM

export const constants = {
  modpacksApi: "https://api.feed-the-beast.com/v1/modpacks",
  metaApi: "https://api.feed-the-beast.com/v1/meta",
  ftbDomain: "https://www.feed-the-beast.com",
  mcHeadApi: "https://api.feed-the-beast.com/v1/lookup/profile/{uuid}/head",
  isProduction: production,
  isDevelopment: !production,
  platform: platform,
}