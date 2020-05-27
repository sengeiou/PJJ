package com.pjj.xsp.module.bean;

/**
 * Created by XinHeng on 2018/12/22.
 * describe：
 */
public class AccessKeyBean extends ResultBean {
    /**
     * data : yrRdCcH3rNNYwWvmv+dFku4bl4XdIGgS4bYZMf8QZ/iT+gU8DWsySYxx5UBmfYv7OxrjyY+gJBa4
     f/TzIUVdx9ERn6KM2amMcWZjX9L4XqUu4aSp9dZbTxICE6kb9vRT8pi+9V89DIzjoZ3nX3Cv0UrP
     XwimNtYT9uJheytjjoJnEHjCwRk+7dYisiYKEHLuPHvdkZrBnm0aX+FYfuepO8+jomk628b/+zp/
     pwgOIqiAv5dXq8JeIiuzfqKEUFJ7QJumC7h/n830J9B53bc/b6awa14Fszb5389YVrZ0wPlr1jzi
     fs0NPQ1SF339WC8FbJawfN35vGfuBDnhwsSvNVIIpFHu3SIZcGKkUMjYul1eu4t7Icksqb1QJCJc
     yvE+eccfzUYe1CmV3flBHGAIyQAwa3ZLCkreThq6gzvziLt1D/FSrYKIiAQZZFYiP5/xMMfFe114
     y2ytQYlzZFjITwgKb8v34tnTzxaYFMUnZJG1/3ANXaYQGPNTn1TPx2iaGxb00R0P7dcvuAh+Spr9
     fzRG3x1FcNmTGNAdEElUn4mPk7ZH5+rOoUGVoy7jGd+hs+3MQyu7n/PjMNHAAJRsGnsGe9Ofm0b+
     0fBHDY5r1VVEen87I89njfmbJGWxfaurjwvgGsSCkTKN9ZE7N7SO7soodMNiKwZ2l8QdMjCA7tiV
     ZE3OelbW2GcvwQ9+PYLCgKipguDh8h/kw6ZcBlskqfPpmZ/0APYgN2BHKqc5WfHelW5bzALeEBP/
     vU+UkW6ogUPLX+86UjZT/4aVmnNzbi8Plp5ptEIEYo0B3R2q17VJavCuZobMtPaQFqVBSNLEXN9o
     s41AcENofhU3/1G+kHrwQNHydSioJsQkJ+HfTZVuA8ZiV2F91bv8oVHPxACDtcfHZqYeXXKSUi3I
     P3z6RwSHJgZo+XzoLwf7QmM5xPmWtA2DcIAtn8gly+b/5cs8NRc+DcnTkDuMrzpzv0G89KtKFX+N
     K/wxGggProoKWVT/pLgrATrjONZx0p9K+4ID346Z6b6+NwFqkCyZa7iSaHFABNJ3TwHsNen8Duox
     72ou5B77wL5F42eglh2+TCEK
     */

    private Cipher data;

    public Cipher getData() {
        return data;
    }

    public void setData(Cipher data) {
        this.data = data;
    }
    public static class Cipher{

        /**
         * accessKeyId : STS.NHMaoQ4zwEbYBbBUbYFvQQ97S
         * securityToken : CAIS3QJ1q6Ft5B2yfSjIr4v4KtXl2aVW8qCyQETzsWIMSvl9vvycsTz2IHxFfXhrAOEZsP01m2xW7fYaloktQs5ISRWcNpQr5cQLq1L7M9bM65G+vLEI0cD5EmHKAUYxQzMS9qGrIunGc9KBNnoN/00amMkJHFfPdlihNoLz1/wDR98LXw6+QCNbDdNNXGVLo9MbMn2jQ5TINQXx0FLNEG1iuAd3lQEa7r+kkOKR9j3UjlDx1pUnxeLLL4OjctNnMbpmTt6xx5YWTKfa1zNK4BU4osUChadfqzDcpYPfURsD+xeEdu7V/MFmNBc8eqUhB8wypfPnx/pjob6Rxcal0xRPeOVUXTjWXJjl3MbfBKTrFK5gLOqiYC+Wj4rSbsWr7Ft5Wx9BalMWIehGA2RrFBkhRgvdLqKa413Qam+hMfPZgfhnisMkkAuxp4XWeQPfG6/31j0DIYMnf5qLjazodIeLGoABXgfcxvtEl2X7v4CzTxGCZ8fPNJzJ6D/pW8GaaaP6j2RrmKjlxL0LA4XIaaeqgDl99gzeGHXUNQg3YBdNh6jGSYA89TVuGU6+tMjjC8JAfjK/s7YNFo/E2Jo5CgQcI49qhFuQOnl2wxw74mH96AAv17cjNp05EtGtT4Pqqi+81nI=
         * bucketName : pjj-liftapp-test
         * accessKeySecret : 4h9ucM93MduUpANXTiXUzunzcGSAtUZ6fJ5kST5Vfdg6
         */

        private String accessKeyId;
        private String securityToken;
        private String bucketName;
        private String accessKeySecret;

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getSecurityToken() {
            return securityToken;
        }

        public void setSecurityToken(String securityToken) {
            this.securityToken = securityToken;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }
    }
}
